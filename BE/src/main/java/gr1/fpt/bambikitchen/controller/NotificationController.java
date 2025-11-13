package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.firebase.model.dto.DeviceTokenRegisterRequest;
import gr1.fpt.bambikitchen.firebase.service.FCMService;
import gr1.fpt.bambikitchen.model.Notification;
import gr1.fpt.bambikitchen.model.dto.request.NotificationSendingRequest;
import gr1.fpt.bambikitchen.model.dto.request.NotificationsSendingManyRequest;
import gr1.fpt.bambikitchen.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService service;
    @Autowired
    private FCMService fcmService;

    @GetMapping
    public List<Notification> getAllNotifications() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("to-account/{id}")
    public List<Notification> getNotificationByAccountId(@PathVariable int id) {
        return service.findByAccount(id);
    }

    @PostMapping
    public Notification createNotification(@Valid @RequestBody Notification notification) {
        return service.save(notification);
    }

    @PutMapping
    public Notification updateNotification(@Valid @RequestBody Notification notification) {
        return service.update(notification);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable int id) {
        service.delete(id);
    }

    @PatchMapping("/{id}/check-read")
    public void markAsRead(@PathVariable int id) {
        service.setRead(id);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotificationToUser(@RequestBody NotificationSendingRequest request) {
        try {
            String response = fcmService.sendNotificationToUser(request.getUserId(), request.getTitle(), request.getMessage());
            return ResponseEntity.ok("Notification sent: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/send-to-all")
    public ResponseEntity<String> sendNotificationToAllUsers(@RequestBody NotificationSendingRequest request) {
        try {
            String response = fcmService.sendNotificationToAllUser(request.getTitle(), request.getMessage());
            return ResponseEntity.ok("Notification sent: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/send-to-exact")
    public ResponseEntity<String> sendNotificationToTheExactDevice(@RequestBody NotificationSendingRequest request) {
        try {
            String response = fcmService.sendNotificationToTheExactDevice(request.getUserId(), request.getDeviceToken(), request.getTitle(), request.getMessage());
            return ResponseEntity.ok("Notification sent: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/device")
    public ResponseEntity<String> registerDeviceToken(@RequestBody DeviceTokenRegisterRequest request) {
        return ResponseEntity.ok(fcmService.registerDeviceToken(request));
    }

    @PostMapping("/send-to-users")
    public ResponseEntity<String> sendNotificationToManyUsers(@RequestBody NotificationsSendingManyRequest request) {
        String response = fcmService.sendNotificationToListUsers(request.getTitle(), request.getMessage(), request.getUserIds());
        return ResponseEntity.ok("Notifications sent: " + response);
    }
}