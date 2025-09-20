package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.enums.DishType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DishUpdateRequest {
    @NotNull
    private Integer id;

    private String name;

    private String description;

    @Min(value = 0, message = "Price must be equal or greater than 0 !!")
    private Integer price;

    private String imageUrl;

    private Integer accountId;

    private Integer categoryId;

    private DishType dishType;

    private Boolean isActive;

    private Boolean isPublic;

    @Min(value = 0, message = "Used quantity must be equal or greater than 0 !!")
    private Integer usedQuantity;
}
