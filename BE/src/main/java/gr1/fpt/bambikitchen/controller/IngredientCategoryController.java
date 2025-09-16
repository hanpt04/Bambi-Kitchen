package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.IngredientCategory;
import gr1.fpt.bambikitchen.service.IngredientCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient-categories")
public class IngredientCategoryController {

    @Autowired
    private IngredientCategoryService service;

    @GetMapping
    public List<IngredientCategory> getAllIngredientCategory() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public IngredientCategory findById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    public IngredientCategory createIngredientCategory(@Valid @RequestBody IngredientCategory ingredientCategory) {
        return service.save(ingredientCategory);
    }

    @PutMapping
    public IngredientCategory updateIngredientCategory(@Valid @RequestBody IngredientCategory ingredientCategory) {
        return service.update(ingredientCategory);
    }

    @DeleteMapping("/{id}")
    public void deleteIngredientCategory(@PathVariable int id) {
        service.delete(id);
    }
}
