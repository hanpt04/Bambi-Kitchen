package gr1.fpt.bambikitchen.service;


import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.Recipe;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.repository.DishRepository;
import gr1.fpt.bambikitchen.repository.IngredientRepository;
import gr1.fpt.bambikitchen.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class DishService {

    @Autowired
    DishRepository dishRepository;

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Transactional
    public Dish save(DishCreateRequest request) {



        Dish dish = Dish.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .dishType(request.getDishType())
                .isActive(true)
                .isPublic(request.isPublic())
                .account(request.getAccount())
                .build();

        if ( request.getId() != null ) {
            dish.setId( request.getId() );
            dish.setActive(request.isActive());
            deleteRecipeWithDishId( request.getId() );
        }


        Dish savedDish = dishRepository.save(dish);
        saveRecipe(request.getIngredients(), savedDish);

        return savedDish;
    }

    public void saveRecipe (Map<Integer, Integer> ingredients, Dish dish)
    {
        ingredients.forEach((ingredientId, quantity) -> {

            Recipe recipe = Recipe.builder()
                    .ingredient(ingredientRepository.getById(ingredientId))
                    .quantity(quantity)
                    .dish(dish)
                    .build();
            recipeRepository.save( recipe);
        });
    }

    public void deleteRecipeWithDishId(int dishId)
    {
        recipeRepository.deleteByDish_Id(dishId);
    }

}
