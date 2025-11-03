package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutritionRepository extends JpaRepository<Nutrition, Integer> {
    Nutrition findByIngredient_Id(int ingredientId);
}
