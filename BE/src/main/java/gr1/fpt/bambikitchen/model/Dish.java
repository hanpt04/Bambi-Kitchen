package gr1.fpt.bambikitchen.model;
import gr1.fpt.bambikitchen.model.enums.DishType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Nationalized
    private String name;
    @Nationalized
    private String description;
    @Min(value = 0,message = "Must be equal or greater than 0 !!")
    private int price;
    private String imageUrl;
    @Column(name = "public_id")
    private String publicId;
    @ManyToOne
    @JoinColumn(name = "AccountId")
    private Account account;
    @Enumerated(EnumType.STRING)
    private DishType dishType;
    private boolean isActive;
    private boolean isPublic;
    @Min(value = 0,message = "Must be equal greater than 0 !!")
    private int usedQuantity;

}
