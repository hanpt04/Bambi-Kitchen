package gr1.fpt.bambikitchen.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import gr1.fpt.bambikitchen.model.Notification;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface NotificationService {
    List<Notification> findAll();
    Notification findById(int id);
    Notification save(Notification notification);
    Notification update(Notification notification);
    void delete(int id);
    void setRead(int id);
    List<Notification> findByAccount(int accountId);

    String sendNotification(String title, String message, Integer userId) throws FirebaseMessagingException, ExecutionException, InterruptedException;

}
