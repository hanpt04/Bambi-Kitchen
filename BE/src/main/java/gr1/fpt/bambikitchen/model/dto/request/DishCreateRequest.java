package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.enums.DishType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DishCreateRequest {

    // Dung chung 1 DTO cho create va update

    @Nullable
    Integer id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    private String description;

    private int price;

    private String imageUrl;

    @NotNull(message = "Account ID must not be null")
    private Account account;


    @NotNull(message = "Dish type must not be null")
    private DishType dishType;

    private boolean isPublic;

    boolean isActive = true;

    @NotNull
    Map<Integer, Integer> ingredients;
}
