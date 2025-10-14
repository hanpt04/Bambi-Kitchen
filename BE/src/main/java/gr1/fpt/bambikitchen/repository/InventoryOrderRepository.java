package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.InventoryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryOrderRepository extends JpaRepository<InventoryOrder, Integer> {
    InventoryOrder findByOrderId(int orderId);
}
