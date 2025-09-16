package gr1.fpt.bambikitchen.service;

import gr1.fpt.bambikitchen.model.IngredientDetail;

import java.util.List;

public interface IngredientDetailService {
    List<IngredientDetail> findAll();
    IngredientDetail findById(int id);
    List<IngredientDetail> findByIngredientId(int id);
    IngredientDetail save(IngredientDetail ingredientDetail);
    IngredientDetail update(IngredientDetail ingredientDetail);
    void delete(int id);
    void setActive(int id);
}

