package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.Recipe;
import gr1.fpt.bambikitchen.model.dto.response.IngredientsGetByDishResponse;
import gr1.fpt.bambikitchen.repository.DishRepository;
import gr1.fpt.bambikitchen.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private DishRepository dishRepository;

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public IngredientsGetByDishResponse findByDishId(int dishId) {
        List<Recipe> recipes = recipeRepository.findByDish_Id(dishId);
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new CustomException("Dish not found", HttpStatus.BAD_REQUEST));
        return IngredientsGetByDishResponse.builder()
                .id(dishId)
                .price(dish.getPrice())
                .name(dish.getName())
                .account(dish.getAccount())
                .description(dish.getDescription())
                .dishType(dish.getDishType())
                .publicId(dish.getPublicId())
                .isPublic(dish.isPublic())
                .imageUrl(dish.getImageUrl())
                .ingredients(
                        recipes.stream().map(Recipe::getIngredient).toList()
                )
                .isActive(dish.isActive())
                .build();
    }

}
