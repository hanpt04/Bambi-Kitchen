package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.dto.request.IngredientCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.IngredientUpdateRequest;
import gr1.fpt.bambikitchen.service.IngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<Ingredient> save(@RequestBody IngredientCreateRequest ingredient) {
        return ResponseEntity.ok(ingredientService.save(ingredient));
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> findAll() {
        return ResponseEntity.ok(ingredientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(ingredientService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Ingredient> findByName(@RequestParam String name) {
        return ResponseEntity.ok(ingredientService.findByName(name));
    }

    @PutMapping
    public ResponseEntity<Ingredient> update(@RequestBody IngredientUpdateRequest ingredient) {
        return ResponseEntity.ok(ingredientService.update(ingredient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(ingredientService.delete(id));
    }
}
