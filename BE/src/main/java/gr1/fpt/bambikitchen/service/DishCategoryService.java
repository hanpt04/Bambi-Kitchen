package gr1.fpt.bambikitchen.service;

import gr1.fpt.bambikitchen.model.dto.request.DishCategoryCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishCategoryUpdateRequest;

import java.util.List;

public interface DishCategoryService {
    DishCategory save(DishCategoryCreateRequest dishCategory);
    List<DishCategory> findAll();
    DishCategory findById(int id);

    DishCategory update(DishCategoryUpdateRequest dishCategory);

    String deleteById(int id);
}
