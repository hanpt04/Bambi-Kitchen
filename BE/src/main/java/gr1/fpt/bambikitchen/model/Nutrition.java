package gr1.fpt.bambikitchen.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "IngredientId")
    private Ingredient ingredient;
    private int calories;
    private float protein;
    private float carb;
    private float fiber;
    private float iron;
    private float sodium;
    private float calcium;
    private float sugar;
    private float sat_fat;
    private float per_unit;
}
