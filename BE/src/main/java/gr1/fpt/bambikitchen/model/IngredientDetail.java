package gr1.fpt.bambikitchen.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "ingredientId")
    @ManyToOne
    private Ingredient ingredient;

    @Column(name = "EntryDate")
    private Date entryDate;

    @FutureOrPresent(message = "Expire Date must not be in the past !")
    private Date expireDate;

    @Min(value = 0,message = "Quantity must be greater than 0 !")
    private int quantity;

    @Column(name="active")
    private boolean active;


}
