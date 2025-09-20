package gr1.fpt.bambikitchen.service;

import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.dto.request.IngredientCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.IngredientUpdateRequest;

import java.util.List;

public interface IngredientService {
    List<Ingredient> findAll();
    Ingredient findById(int id);
    Ingredient findByName(String name);
    Ingredient save(IngredientCreateRequest ingredient);
    Ingredient update(IngredientUpdateRequest ingredient);
    String delete(int id);
}
