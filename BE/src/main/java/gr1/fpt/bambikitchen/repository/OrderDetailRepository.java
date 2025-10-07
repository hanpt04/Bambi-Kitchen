package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
}
