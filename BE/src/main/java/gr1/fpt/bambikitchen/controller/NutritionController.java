package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Nutrition;
import gr1.fpt.bambikitchen.service.impl.NutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping ("api/nutrition")
public class NutritionController {
    private final NutritionService nutritionService;


    @GetMapping
    public Nutrition findNutritionById (@RequestParam int id){
        return nutritionService.getNutritionById(id);
    }

    @PostMapping
    public Nutrition createNutrition (@RequestBody Nutrition nutrition){
        return nutritionService.createNutrition(nutrition);
    }

    @PutMapping
    public Nutrition updateNutrition (@RequestBody Nutrition nutrition){
        return nutritionService.updateNutrition(nutrition);
    }
}
