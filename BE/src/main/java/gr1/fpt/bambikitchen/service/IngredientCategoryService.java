package gr1.fpt.bambikitchen.service;


import gr1.fpt.bambikitchen.model.IngredientCategory;

import java.util.List;

public interface IngredientCategoryService {
    List<IngredientCategory> findAll();
    IngredientCategory findById(int id);
    IngredientCategory save(IngredientCategory ingredientCategory);
    IngredientCategory update(IngredientCategory ingredientCategory);
    void delete(int id);
}


