package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    void deleteByDish_Id(int dishId);

}
