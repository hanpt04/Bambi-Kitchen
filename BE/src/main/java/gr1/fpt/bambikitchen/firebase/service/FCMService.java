package gr1.fpt.bambikitchen.firebase.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.*;
import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.firebase.model.DeviceToken;
import gr1.fpt.bambikitchen.firebase.model.dto.DeviceTokenRegisterRequest;
import gr1.fpt.bambikitchen.firebase.repository.DeviceTokenRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class FCMService {

    // wired bean
    private final DeviceTokenRepository deviceTokenRepository;

    // constraints - tránh hardcode
    private final int RETRY_COUNT = 3;

    public String sendNotificationToTheExactDevice(Integer userId, String token, String title, String body) throws FirebaseMessagingException {
        DeviceToken deviceToken = deviceTokenRepository.findByUserIdAndToken(userId, token).orElseThrow(
                () -> new CustomException("Token not found", HttpStatus.BAD_REQUEST)
        );

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(deviceToken.getToken())
                .setNotification(notification)
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }

    public String sendNotificationToUser(Integer userId, String title, String body) throws ExecutionException, InterruptedException {
        // một người dùng có thể có nhiều thiết bị
        List<String> tokens = deviceTokenRepository.findByUserId(userId).parallelStream()
                .map(DeviceToken::getToken)
                .toList();

        if (tokens.isEmpty()) {
            throw new CustomException("No tokens found", HttpStatus.BAD_REQUEST);
        }

        return sendMultipleNotification(title, body, tokens);
    }

    public String sendNotificationToAllUser(String title, String body) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        List<String> tokens = deviceTokenRepository.findAll().parallelStream()
                .map(DeviceToken::getToken)
                .toList();

        if (tokens.isEmpty()) {
            throw new CustomException("No tokens found", HttpStatus.BAD_REQUEST);
        }

        return sendMultipleNotification(title, body, tokens);
    }

    public String registerDeviceToken(DeviceTokenRegisterRequest request) {
        // Check xem token đã tồn tại cho user này chưa
        DeviceToken token = deviceTokenRepository.findByToken(request.getToken()).orElse(new DeviceToken());
        token.setToken(request.getToken());
        token.setPlatform(request.getPlatform());
        token.setUserId(request.getUserId());
        token.setActive(true);
        token.setLastTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        deviceTokenRepository.save(token);
        return "OK";
    }

    private String sendMultipleNotification(String title, String body, List<String> tokens) throws ExecutionException, InterruptedException {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        // lấy những token gửi thất bại
        List<String> failedTokens = new ArrayList<>();
        CompletableFuture<BatchResponse> completableFuture = sendBatchResponseWithCompletableFuture(tokens, notification);

        // handle async
        completableFuture.thenAccept(batchResponse -> {
            List<SendResponse> responses = batchResponse.getResponses();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    failedTokens.add(tokens.get(i));
                }
            }
        });

        // chờ kết quả của future
        completableFuture.join();

        // nếu có thất bại thì thử send lại 3 lần nữa
        SendingResult result = new SendingResult();
        if (!failedTokens.isEmpty()) {
            result = retrySending(failedTokens, notification);
        }

        // để response
        AtomicInteger successfulTasksCount = new AtomicInteger(tokens.size() - failedTokens.size());
        AtomicInteger failedTasksCount = new AtomicInteger();

        // xóa nếu token không thực hiện được gửi notification sau 3 lần gửi lại
        result.getResults().forEach((token, isSuccess) -> {
            if (!isSuccess) {
                failedTasksCount.getAndAdd(1);
                eliminateInvalidTokens(token);
            } else successfulTasksCount.getAndAdd(1);
        });

        return "Successfully sent message to " + successfulTasksCount.get() + " devices\n" +
                "Failed to send message to " + failedTasksCount.get() + " devices";
    }

    private SendingResult retrySending(List<String> tokens, Notification notification) {
        SendingResult result = new SendingResult();
        tokens.forEach(token -> result.getResults().put(token, false));

        for (int i = 0; i < RETRY_COUNT; i++) {
            // nếu là vòng lặp đầu thì lấy toàn bộ failedTokens, không thì lấy những tokens bị failed thôi
            CompletableFuture<BatchResponse> completableFuture;
            List<String> failedTokens;
            if (i == 0) {
                failedTokens = new ArrayList<>(tokens);
            } else {
                failedTokens = result.getResults().entrySet().stream()
                        .filter(e -> !e.getValue())
                        .map(Map.Entry::getKey)
                        .toList();
            }

            // nếu không còn token hỏng nữa thì break
            if (failedTokens.isEmpty()) {
                break;
            }

            completableFuture = sendBatchResponseWithCompletableFuture(failedTokens, notification);

            completableFuture.thenAccept(batchResponse -> {
                List<SendResponse> responses = batchResponse.getResponses();
                for (int j = 0; j < responses.size(); j++) {
                    // nếu đã gửi thành công thì xóa nó ra khỏi failed list
                    if (responses.get(j).isSuccessful()) {
                        result.getResults().put(failedTokens.get(j), true);
                    }
                }
            }).join();
        }
        return result;
    }

    private void eliminateInvalidTokens(String token) {
        deviceTokenRepository.findByToken(token).ifPresent(dt -> {
            dt.setActive(false);
            deviceTokenRepository.save(dt);
        });
    }

    // đổi sang CompletableFuture để dễ handle async result
    private CompletableFuture<BatchResponse> sendBatchResponseWithCompletableFuture(List<String> tokens, Notification notification) {
        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(notification)
                .build();

        ApiFuture<BatchResponse> response = FirebaseMessaging.getInstance().sendEachForMulticastAsync(message);

        CompletableFuture<BatchResponse> completableFuture = new CompletableFuture<>();
        response.addListener(() -> {
            try {
                completableFuture.complete(response.get());
            } catch (InterruptedException | ExecutionException e) {
                completableFuture.completeExceptionally(e);
            }
        }, Runnable::run);
        return completableFuture;
    }

    @Data
    private static class SendingResult {
        // key: token, value: isSuccess
        Map<String, Boolean> results = new HashMap<>();
    }
}
