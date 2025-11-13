package gr1.fpt.bambikitchen.firebase.service;

import com.google.gson.Gson;
import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.firebase.model.DeviceToken;
import gr1.fpt.bambikitchen.firebase.model.dto.DeviceTokenRegisterRequest;
import gr1.fpt.bambikitchen.firebase.repository.DeviceTokenRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class FCMService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Gson gson = new Gson();

    // Expo Push API endpoint
    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";
    private static final int RETRY_COUNT = 3;
    private static final int BATCH_SIZE = 100; // Expo giới hạn 100 notifications/request

    public String sendNotificationToTheExactDevice(Integer userId, String token, String title, String body) {
        DeviceToken deviceToken = deviceTokenRepository.findByUserIdAndToken(userId, token).orElseThrow(
                () -> new CustomException("Token not found", HttpStatus.BAD_REQUEST)
        );

        ExpoMessage message = ExpoMessage.builder()
                .to(deviceToken.getToken())
                .title(title)
                .body(body)
                .sound("default")
                .build();

        return sendToExpo(Collections.singletonList(message));
    }

    public String sendNotificationToUser(Integer userId, String title, String body) {
        List<String> tokens = deviceTokenRepository.findByUserId(userId).parallelStream()
                .map(DeviceToken::getToken)
                .toList();

        if (tokens.isEmpty()) {
            throw new CustomException("No tokens found", HttpStatus.BAD_REQUEST);
        }

        return sendMultipleNotification(title, body, tokens);
    }

    public String sendNotificationToAllUser(String title, String body) {
        List<String> tokens = deviceTokenRepository.findAll().parallelStream()
                .map(DeviceToken::getToken)
                .toList();

        if (tokens.isEmpty()) {
            throw new CustomException("No tokens found", HttpStatus.BAD_REQUEST);
        }

        return sendMultipleNotification(title, body, tokens);
    }

    public String sendNotificationToListUsers(String title, String body, List<Integer> userIds) {
        List<String> tokens = deviceTokenRepository.findAll().parallelStream()
                .filter(dt -> userIds.contains(dt.getUserId()))
                .map(DeviceToken::getToken)
                .toList();

        if (tokens.isEmpty()) {
            throw new CustomException("No tokens found", HttpStatus.BAD_REQUEST);
        }

        return sendMultipleNotification(title, body, tokens);
    }

    public String registerDeviceToken(DeviceTokenRegisterRequest request) {// Validate Expo push token format
        if (!isValidExpoToken(request.getToken())) {
            throw new CustomException("Invalid Expo push token format", HttpStatus.BAD_REQUEST);
        }

        DeviceToken token = deviceTokenRepository.findByToken(request.getToken())
                .orElse(new DeviceToken());
        token.setToken(request.getToken());
        token.setPlatform(request.getPlatform());
        token.setUserId(request.getUserId());
        token.setActive(true);
        token.setLastTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        deviceTokenRepository.save(token);
        return "OK";
    }

    private String sendMultipleNotification(String title, String body, List<String> tokens) {
        // Chia tokens thành các batch nhỏ (Expo giới hạn 100 notifications/request)
        List<List<String>> batches = partitionList(tokens, BATCH_SIZE);

        List<String> allFailedTokens = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger(0);

        // Gửi từng batch
        for (List<String> batch : batches) {
            List<ExpoMessage> messages = batch.stream()
                    .map(token -> ExpoMessage.builder()
                            .to(token)
                            .title(title)
                            .body(body)
                            .sound("default")
                            .priority("high")
                            .build())
                    .toList();

            try {
                ExpoResponse response = sendToExpoWithResponse(messages);

                // Xử lý kết quả
                for (int i = 0; i < response.getData().size(); i++) {
                    ExpoTicket ticket = response.getData().get(i);
                    if ("ok".equals(ticket.getStatus())) {
                        successCount.incrementAndGet();
                    } else {
                        allFailedTokens.add(batch.get(i));
                        // Log lỗi nếu có
                        if (ticket.getDetails() != null) {
                            System.err.println("Failed to send to " + batch.get(i) + ": " +
                                    ticket.getDetails().getError());
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Batch sending failed: " + e.getMessage());
                allFailedTokens.addAll(batch);
            }
        }

        // Retry failed tokens
        SendingResult retryResult = new SendingResult();
        if (!allFailedTokens.isEmpty()) {
            retryResult = retrySending(allFailedTokens, title, body);
        }

        // Tính toán kết quả cuối cùng
        AtomicInteger finalSuccessCount = new AtomicInteger(successCount.get());
        AtomicInteger finalFailedCount = new AtomicInteger(0);

        retryResult.getResults().forEach((token, isSuccess) -> {
            if (!isSuccess) {finalFailedCount.incrementAndGet();
                eliminateInvalidTokens(token);
            } else {
                finalSuccessCount.incrementAndGet();
            }
        });

        return "Successfully sent message to " + finalSuccessCount.get() + " devices\n" +
                "Failed to send message to " + finalFailedCount.get() + " devices";
    }

    private SendingResult retrySending(List<String> tokens, String title, String body) {
        SendingResult result = new SendingResult();
        tokens.forEach(token -> result.getResults().put(token, false));

        for (int i = 0; i < RETRY_COUNT; i++) {
            List<String> failedTokens = i == 0 ? new ArrayList<>(tokens) :
                    result.getResults().entrySet().stream()
                            .filter(e -> !e.getValue())
                            .map(Map.Entry::getKey)
                            .toList();

            if (failedTokens.isEmpty()) {
                break;
            }

            List<ExpoMessage> messages = failedTokens.stream()
                    .map(token -> ExpoMessage.builder()
                            .to(token)
                            .title(title)
                            .body(body)
                            .sound("default")
                            .build())
                    .toList();

            try {
                ExpoResponse response = sendToExpoWithResponse(messages);
                for (int j = 0; j < response.getData().size(); j++) {
                    if ("ok".equals(response.getData().get(j).getStatus())) {
                        result.getResults().put(failedTokens.get(j), true);
                    }
                }
            } catch (Exception e) {
                System.err.println("Retry failed: " + e.getMessage());
            }
        }

        return result;
    }

    private String sendToExpo(List<ExpoMessage> messages) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String jsonPayload = gson.toJson(messages);
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            String response = restTemplate.postForObject(EXPO_PUSH_URL, request, String.class);
            return response != null ? response : "Sent successfully";
        } catch (Exception e) {
            throw new CustomException("Failed to send notification: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ExpoResponse sendToExpoWithResponse(List<ExpoMessage> messages) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String jsonPayload = gson.toJson(messages);HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            return restTemplate.postForObject(EXPO_PUSH_URL, request, ExpoResponse.class);
        } catch (Exception e) {
            throw new CustomException("Failed to send notification: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void eliminateInvalidTokens(String token) {
        deviceTokenRepository.findByToken(token).ifPresent(dt -> {
            dt.setActive(false);
            deviceTokenRepository.save(dt);
        });
    }

    private boolean isValidExpoToken(String token) {
        // Expo push token format: ExponentPushToken[xxxxxxxxxxxxxxxxxxxxxx]
        return token != null && token.startsWith("ExponentPushToken[") && token.endsWith("]");
    }

    private <T> List<List<T>> partitionList(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }

    // DTOs for Expo Push API
    @Data
    @lombok.Builder
    private static class ExpoMessage {
        private String to;
        private String title;
        private String body;
        private String sound;
        private String priority;
        private Map<String, Object> data;
    }

    @Data
    private static class ExpoResponse {
        private List<ExpoTicket> data;
    }

    @Data
    private static class ExpoTicket {
        private String status; // "ok" or "error"
        private String id;
        private ExpoError details;
    }

    @Data
    private static class ExpoError {
        private String error;
        private String message;
    }

    @Data
    private static class SendingResult {
        private Map<String, Boolean> results = new HashMap<>();
    }
}