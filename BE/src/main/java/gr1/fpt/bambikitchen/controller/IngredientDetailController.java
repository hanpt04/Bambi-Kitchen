package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.IngredientDetail;
import gr1.fpt.bambikitchen.service.IngredientDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient-details")
public class IngredientDetailController {

    @Autowired
    private IngredientDetailService service;

    @GetMapping
    public List<IngredientDetail> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public IngredientDetail getById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("/ingredient/{ingredientId}")
    public List<IngredientDetail> getByIngredientId(@PathVariable int ingredientId) {
        return service.findByIngredientId(ingredientId);
    }

    @PostMapping
    public IngredientDetail create(@Valid @RequestBody IngredientDetail ingredientDetail) {
        return service.save(ingredientDetail);
    }

    @PutMapping
    public IngredientDetail update(@Valid @RequestBody IngredientDetail ingredientDetail) {
        return service.update(ingredientDetail);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @PatchMapping("/toggle-active/{id}")
    public void toggleActive(@PathVariable int id) {
        service.setActive(id);
    }
}

