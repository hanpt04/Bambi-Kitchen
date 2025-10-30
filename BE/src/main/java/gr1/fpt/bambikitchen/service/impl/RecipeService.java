package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.model.Recipe;
import gr1.fpt.bambikitchen.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> findByDishId(int dishId) {
        return recipeRepository.findByDish_Id(dishId);
    }

}
