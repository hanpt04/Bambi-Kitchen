package gr1.fpt.bambikitchen.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="IngredientCategories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name can not blank !")
    @Length(min = 3, message = "Name must greater than 3 chars")
    @Nationalized
    private String name;
    @Nationalized
    private String description;
}
