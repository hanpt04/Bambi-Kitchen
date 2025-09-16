package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Notification;
import gr1.fpt.bambikitchen.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping
    public List<Notification> getAllNotifications() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("account/{id}")
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

    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable int id) {
        service.setRead(id);
    }
}
