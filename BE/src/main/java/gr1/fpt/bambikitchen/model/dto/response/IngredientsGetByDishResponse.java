package gr1.fpt.bambikitchen.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.IngredientCategory;
import gr1.fpt.bambikitchen.model.enums.DishType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class IngredientsGetByDishResponse {
    private int id;
    private String name;
    private String description;
    private int price;
    private String imageUrl;
    private String publicId;
    private Account account;
    private List<IngredientDetail> ingredients;
    private DishType dishType;
    private boolean isActive;
    private boolean isPublic;

    @Builder
    @Data
    public static class IngredientDetail {
        private int id;
        private String name;
        private Double storedQuantity;
        private Integer neededQuantity;
        private IngredientCategory category;
        private String imageUrl;
    }
}
