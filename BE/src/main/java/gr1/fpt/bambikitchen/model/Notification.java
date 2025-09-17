package gr1.fpt.bambikitchen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Nationalized
    private String title;
    @Nationalized
    private String message;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private boolean isRead = false;
    @ManyToOne
    @JoinColumn(name = "AccountId")
    private Account account;

}
