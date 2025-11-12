package gr1.fpt.bambikitchen.model.dto.response;

import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.Nutrition;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IngredientWithNutritionResponse {
    private Ingredient ingredient;
    private Nutrition nutrition;
}
