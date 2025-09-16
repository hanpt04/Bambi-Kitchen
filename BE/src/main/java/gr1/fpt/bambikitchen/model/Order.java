package gr1.fpt.bambikitchen.model;
import gr1.fpt.bambikitchen.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private LocalDateTime createAt;
    private int totalPrice;
    private OrderStatus status;
    private int userId;
    private int staffId;
    private String note;
    private int raking;
    private int comment;
}
