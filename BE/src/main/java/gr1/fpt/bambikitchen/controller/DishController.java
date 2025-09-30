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
@CrossOrigin(origins = "*")
public class DishController {

    private final DishService dishService;

    @PostMapping
    public ResponseEntity<Dish> save(@RequestBody DishCreateRequest request){
        System.out.println(request);
        return ResponseEntity.ok(dishService.save(request));
    }



}
