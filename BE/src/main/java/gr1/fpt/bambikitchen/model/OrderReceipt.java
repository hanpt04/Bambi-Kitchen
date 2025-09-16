package gr1.fpt.bambikitchen.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name ="IngredientId")
    private Ingredient ingredient;
    @ManyToOne
    @JoinColumn(name ="OrderDetailId")
    private OrderDetail orderDetail;
    private int quantity;
}
