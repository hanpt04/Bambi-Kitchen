package gr1.fpt.bambikitchen.model;
import gr1.fpt.bambikitchen.model.enums.DishType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private int price;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "AccountId")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private DishCategory category;
    private DishType dishType;
    private boolean isActive;
    private boolean isPublic;
    private int usedQuantity;


}
