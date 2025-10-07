package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder_OrderId(int orderOrderId);

    void deleteAllByOrOrder_OrderId(int orderOrderId);
}
