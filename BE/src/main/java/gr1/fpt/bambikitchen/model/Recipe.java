package gr1.fpt.bambikitchen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name ="IngredientId")
    private Ingredient ingredient;
    private int quantity;
    @ManyToOne
    @JoinColumn(name ="DishId")
    private Dish dish;

}
