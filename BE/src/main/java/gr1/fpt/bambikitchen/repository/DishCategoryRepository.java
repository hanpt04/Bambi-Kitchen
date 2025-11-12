package gr1.fpt.bambikitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishCategoryRepository extends JpaRepository<DishCategory,Integer> {
    boolean existsByName(String name);
}
