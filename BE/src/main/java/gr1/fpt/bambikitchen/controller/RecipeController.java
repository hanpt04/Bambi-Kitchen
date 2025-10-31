package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Recipe;
import gr1.fpt.bambikitchen.model.dto.response.IngredientsGetByDishResponse;
import gr1.fpt.bambikitchen.service.impl.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<Recipe>> findAll() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }
    @GetMapping("/by-dish/{id}")
    public ResponseEntity<IngredientsGetByDishResponse> findByDishId(@PathVariable int id) {
        return ResponseEntity.ok(recipeService.findByDishId(id));
    }
}
