package gr1.fpt.bambikitchen.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

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
    private Orders orders;
    private int totalCalories;
    @Nationalized
    private String notes;
}
