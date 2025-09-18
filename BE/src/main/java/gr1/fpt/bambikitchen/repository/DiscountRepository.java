package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer> {
    boolean existsByCode(String code);
}
