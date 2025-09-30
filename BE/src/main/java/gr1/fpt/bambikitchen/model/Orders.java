package gr1.fpt.bambikitchen.model;
import gr1.fpt.bambikitchen.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @CreationTimestamp
    private LocalDateTime createAt;
    private int totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private int userId;
    private int staffId;
    @Nationalized
    private String note;
    private int ranking;
    @Nationalized
    private int comment;
}
