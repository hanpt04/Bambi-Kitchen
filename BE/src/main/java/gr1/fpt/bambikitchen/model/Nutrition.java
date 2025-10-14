package gr1.fpt.bambikitchen.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Nutrition {
    @Id
    int id;
    @OneToOne
    @MapsId // Ánh xạ id của Nutrition từ id của Ingredient
    @JoinColumn(name = "id") // Tên cột trong bảng nutrition sẽ là "id"
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
    private String per_unit;
}
