package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUserId(int userId);
}
