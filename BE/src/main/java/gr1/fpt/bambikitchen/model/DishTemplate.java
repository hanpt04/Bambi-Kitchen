package gr1.fpt.bambikitchen.model;


import gr1.fpt.bambikitchen.model.enums.SizeCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishTemplate {

    @Id
    @Enumerated(EnumType.STRING)
    private SizeCode size;
    @Nationalized
    private String name;
    double priceRatio;
    double quantityRatio;
    int max_Carb;
    int max_Protein;
    int max_Vegetable;
}




