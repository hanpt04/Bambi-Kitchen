package gr1.fpt.bambikitchen.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name ="DishId")
    private Dish dish;
    @ManyToOne
    @JoinColumn(name ="OrderId")
    private Order order;
    private int totalCalories;
    private String notes;
}
