package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish,Integer> {
}
