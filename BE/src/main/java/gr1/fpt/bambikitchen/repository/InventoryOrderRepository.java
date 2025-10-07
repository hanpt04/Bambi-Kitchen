package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.InventoryOrder;
import gr1.fpt.bambikitchen.service.InventoryOrderService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryOrderRepository extends JpaRepository<InventoryOrder, Integer> {
    InventoryOrder findByOrderId(int orderId);
}
