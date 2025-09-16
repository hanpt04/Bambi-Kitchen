package gr1.fpt.bambikitchen.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Nationalized
    private String name;
    @Min(value = 0, message = "Discount Percent must be greater than 0 !!")
    @Max(value = 100, message = "Discount Percent must be less than 100 !!")
    private int discountPercent;
    @Min(value = 0, message = "Quantity must be greater than 0 !!")
    private int quantity;
    private Date startTime;
    private Date endTime;
    @Size(min = 3, message = "At least 3 characters")
    private String code;
    @Nationalized
    private String description;

}
