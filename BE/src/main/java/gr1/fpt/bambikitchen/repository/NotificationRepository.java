package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByAccount_Id(int id);
}
