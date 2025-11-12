package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.model.Nutrition;
import gr1.fpt.bambikitchen.repository.NutritionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NutritionService {
    private final NutritionRepository nutritionRepository;


    public Nutrition getNutritionById(int id) {
        return nutritionRepository.findById(id).orElse(null);
    }

    public Nutrition createNutrition(Nutrition nutrition) {
        return nutritionRepository.save(nutrition);
    }

    public Nutrition updateNutrition(Nutrition nutrition) {
        return nutritionRepository.save(nutrition);
    }
}
