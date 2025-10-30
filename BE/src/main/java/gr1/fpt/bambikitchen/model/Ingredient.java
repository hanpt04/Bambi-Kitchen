package gr1.fpt.bambikitchen.model;

import gr1.fpt.bambikitchen.model.enums.Unit;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name can not blank !")
    @Nationalized
    private String name;

    @JoinColumn(name = "cateId")
    @ManyToOne
    private IngredientCategory category;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Column(name="active")
    private boolean active = true;

    @Column(name = "imgUrl" )
    private String imgUrl;

    @Column(name = "public_id")
    private String publicId;

    //số lượng thực tế còn lại trong kho
    @Column(name = "quantity")
    private Double quantity;

    //số lượng mà đang giữ chỗ
    @Column(name = "reserve")
    private Double reserve = 0.0;

    @Column(name = "last_reserve_at")
    @CreationTimestamp
    private Date lastReserveAt;

    @Column(name = "available_quantity")
    private Double available;

    @Column(name = "price_per_unit")
    private double pricePerUnit;//giá nhập vào theo đơn vị

    //số lượng mà khả dụng cho các order tiếp theo
    public double availableIngredient(){
        return quantity - reserve;
    }
}
