package gr1.fpt.bambikitchen.model.dto.request;


import gr1.fpt.bambikitchen.model.enums.SourceType;
import lombok.Data;

@Data
public class RecipeItemDTO {

    int ingredientId;
    int quantity;
    SourceType sourceType; // BASE, ADDON, REMOVED
}
