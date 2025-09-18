package gr1.fpt.bambikitchen.service;

import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishUpdateRequest;

import java.util.List;

public interface DishService {
    Dish save(DishCreateRequest account);
    List<Dish> findAll();
    Dish findById(int id);

    Dish update(DishUpdateRequest account);

    String deleteById(int id);
}
