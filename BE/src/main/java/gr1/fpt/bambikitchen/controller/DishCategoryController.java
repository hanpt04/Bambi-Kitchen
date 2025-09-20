package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.DishCategory;
import gr1.fpt.bambikitchen.model.dto.request.DishCategoryCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishCategoryUpdateRequest;
import gr1.fpt.bambikitchen.service.DishCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/dish-category")
public class DishCategoryController {

    private final DishCategoryService dishCategoryService;

    @PostMapping
    public ResponseEntity<DishCategory> save(@RequestBody DishCategoryCreateRequest discount){
        return ResponseEntity.ok(dishCategoryService.save(discount));
    }

    @GetMapping
    public ResponseEntity<List<DishCategory>> findAll(){
        return ResponseEntity.ok(dishCategoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishCategory> findById(@PathVariable Integer id){
        return ResponseEntity.ok(dishCategoryService.findById(id));
    }

    @PutMapping
    public ResponseEntity<DishCategory> update(@RequestBody DishCategoryUpdateRequest discount){
        return ResponseEntity.ok(dishCategoryService.update(discount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        return ResponseEntity.ok(dishCategoryService.deleteById(id));
    }
}
