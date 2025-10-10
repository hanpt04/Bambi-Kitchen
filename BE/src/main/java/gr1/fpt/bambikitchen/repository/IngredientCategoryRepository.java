package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Integer> {
    boolean existsByName(String name);
}
