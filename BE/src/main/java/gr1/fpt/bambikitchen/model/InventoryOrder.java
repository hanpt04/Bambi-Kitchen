package gr1.fpt.bambikitchen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class InventoryOrder {
    @Id
    private int orderId;
    @CreationTimestamp
    private Timestamp receivedAt;
}
