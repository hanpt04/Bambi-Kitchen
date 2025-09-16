package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.IngredientDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientDetailRepository extends JpaRepository<IngredientDetail, Integer> {
    List<IngredientDetail> findByIngredientId(int id);
}
