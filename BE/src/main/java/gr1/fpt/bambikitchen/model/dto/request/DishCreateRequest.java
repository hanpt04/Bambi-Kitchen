package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.enums.DishType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DishCreateRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;

    private String description;

    @Min(value = 0, message = "Price must be equal or greater than 0 !!")
    private int price;

    private String imageUrl;

    @NotNull(message = "Account ID must not be null")
    private Integer accountId;

    @NotNull(message = "Category ID must not be null")
    private Integer categoryId;

    @NotNull(message = "Dish type must not be null")
    private DishType dishType;

    private boolean isActive;

    private boolean isPublic;

    @Min(value = 0, message = "Used quantity must be equal or greater than 0 !!")
    private int usedQuantity;
}
