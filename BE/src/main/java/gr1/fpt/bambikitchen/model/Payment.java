package gr1.fpt.bambikitchen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    int orderId;
    int accountId;
    Long amount;
    String paymentMethod;
    String status = "PENDING"; // PENDING, COMPLETED, FAILED
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;
    String transactionId;


    public Payment(int orderId, int accountId, String method, Long amount) {
        this.orderId = orderId;
        this.accountId = accountId;
        this.paymentMethod = method;
        this.amount = amount;
    }
}
