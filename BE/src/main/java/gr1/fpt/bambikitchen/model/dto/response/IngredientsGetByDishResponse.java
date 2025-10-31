package gr1.fpt.bambikitchen.model.dto.response;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.enums.DishType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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
    private List<Ingredient> ingredients;
    private DishType dishType;
    private boolean isActive;
    private boolean isPublic;
}
