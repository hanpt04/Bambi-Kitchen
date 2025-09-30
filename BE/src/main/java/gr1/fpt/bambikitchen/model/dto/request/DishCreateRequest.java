package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.enums.DishType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DishCreateRequest {


    @NotBlank(message = "Name must not be blank")
    private String name;

    private String description;

    @Min(value = 0, message = "Price must be equal or greater than 0 !!")
    private int price;

    private String imageUrl;

    @NotNull(message = "Account ID must not be null")
    private Account account;


    @NotNull(message = "Dish type must not be null")
    private DishType dishType;

    private boolean isPublic;

    @NotNull
    Map<Integer, Integer> ingredients;
}
