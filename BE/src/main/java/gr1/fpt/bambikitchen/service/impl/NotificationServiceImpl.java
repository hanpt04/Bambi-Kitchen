package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.Notification;
import gr1.fpt.bambikitchen.repository.NotificationRepository;
import gr1.fpt.bambikitchen.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository repo;

    @Override
    public List<Notification> findAll() {
        return repo.findAll();
    }

    @Override
    public Notification findById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Notification not found!", HttpStatus.NOT_FOUND));
    }

    @Override
    public Notification save(Notification notification) {
        return repo.save(notification);
    }

    @Override
    public Notification update(Notification notification) {
        if (repo.existsById(notification.getId())) {
            return repo.save(notification);
        } else {
            throw new CustomException("Notification not found!", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(int id) {
        if (!repo.existsById(id)) {
            throw new CustomException("Notification not found!", HttpStatus.NOT_FOUND);
        }
        repo.deleteById(id);
    }

    @Override
    public void setRead(int id) {
        Notification notification = repo.findById(id)
                .orElseThrow(() -> new CustomException("Notification not found!", HttpStatus.NOT_FOUND));
        notification.setRead(true);
        repo.save(notification);
    }

    @Override
    public List<Notification> findByAccount(int accountId) {
        return repo.findByAccount_Id(accountId);
    }
}
