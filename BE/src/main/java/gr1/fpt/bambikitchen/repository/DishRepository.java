package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish,Integer> {
    List<Dish> findByAccount_Id(int accountId);

    List<Dish> findAllByIsActiveAndIsPublic(boolean active, boolean isPublic);
    List<Dish> findTop5ByUsedQuantityDesc();
}
