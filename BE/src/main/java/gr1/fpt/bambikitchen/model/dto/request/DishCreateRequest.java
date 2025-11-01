package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.enums.DishType;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Nationalized;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
public class    DishCreateRequest {

    // Dung chung 1 DTO cho create va update

    @Nullable
    Integer id;
    @Nationalized
    @NotBlank(message = "Name must not be blank")
    private String name;
    @Nationalized
    private String description;

    private int price;

    @NotNull(message = "Account ID must not be null")
    private Account account;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Dish type must not be null")
    private DishType dishType;

    private boolean isPublic;

    boolean isActive = true;

    //IngreId, Quantity
    @NotNull
    Map<Integer, Integer> ingredients;
    @Min(value = 0,message = "Must be equal greater than 0 !!")
    private int usedQuantity;
    @Nullable
    MultipartFile file ;
}
