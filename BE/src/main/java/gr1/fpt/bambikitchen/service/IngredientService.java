package gr1.fpt.bambikitchen.service;

import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.dto.request.IngredientCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.IngredientUpdateRequest;
import gr1.fpt.bambikitchen.model.dto.response.IngredientWithNutritionResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IngredientService {
    List<Ingredient> findAll();
    Ingredient findById(int id);
    Ingredient findByName(String name);
    IngredientWithNutritionResponse save(IngredientCreateRequest ingredient) throws IOException;
    Ingredient update(IngredientUpdateRequest ingredient) throws IOException;
    String delete(int id);
    boolean isEnoughIngredient(Map<Integer,Double> ingredientMap, int orderId );
    boolean checkAvailable(Map<Integer,Double> ingredientMap, int orderId);
    public void minusInventory(int orderId);
    public void resetReserve(int orderId);
    public void saveOrder(int orderId);
    public void toggleActive(int id);
    public List<Ingredient> getLowInventoryIngredients();
    List<Ingredient> findAllActive();
}
