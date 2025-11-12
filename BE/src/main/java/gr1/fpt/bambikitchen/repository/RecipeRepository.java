package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    void deleteByDish_Id(int dishId);

    List<Recipe> getIngredientsByDish_Id(int dishId);

    List<Recipe> findByDish(Dish dish);

    List<Recipe> findByDish_Id(int dishId);

    List<Recipe> getIngredientsByIngredient_Id(int ingredientId);
}
