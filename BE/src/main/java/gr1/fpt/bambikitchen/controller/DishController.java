package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishUpdateRequest;
import gr1.fpt.bambikitchen.service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/dish")
public class DishController {

    private final DishService dishService;

    @PostMapping
    public ResponseEntity<Dish> save(@RequestBody DishCreateRequest discount){
        return ResponseEntity.ok(dishService.save(discount));
    }

    @GetMapping
    public ResponseEntity<List<Dish>> findAll(){
        return ResponseEntity.ok(dishService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> findById(@PathVariable int id){
        return ResponseEntity.ok(dishService.findById(id));
    }

    @PutMapping
    public ResponseEntity<Dish> update(@RequestBody DishUpdateRequest discount){
        return ResponseEntity.ok(dishService.update(discount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
        return ResponseEntity.ok(dishService.deleteById(id));
    }

}
