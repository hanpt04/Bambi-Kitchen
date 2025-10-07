package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Ingredient;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,Integer> {
    Optional<Ingredient> findByName(String name);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Ingredient i WHERE i.id= :id")
    Ingredient lockById(@Param("id") int id);
}
