package gr1.fpt.bambikitchen.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name ="IngredientId")
    private Ingredient ingredient;

    //Element để tự động ORM thêm 1 bảng phụ dưới DB
    @ElementCollection
    @CollectionTable(name = "nutrition")
    @MapKeyColumn(name = "nutrition_name")
    @Column(name = "quantity")
    private Map<String, Integer> items = new HashMap<>();

}
