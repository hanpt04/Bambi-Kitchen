package gr1.fpt.bambikitchen.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "ingredientId")
    @ManyToOne
    private Ingredient ingredient;

    @Nullable
    @JoinColumn(name = "OrderId")
    @ManyToOne
    private Orders orders;

    @CreationTimestamp
    private LocalDateTime createAt;

    @Min(value = 0,message = "Quantity must be greater than 0 !")
    private int quantity;

    @NotNull(message = "Type must not be null !")
    private boolean transactionType;
    //true: nhap
    //false :xuat

}
