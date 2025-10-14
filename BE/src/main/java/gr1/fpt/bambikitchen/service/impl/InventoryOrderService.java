package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.model.InventoryOrder;
import gr1.fpt.bambikitchen.repository.InventoryOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryOrderService {
    @Autowired
    private InventoryOrderRepository inventoryOrderRepository;

    public List<InventoryOrder> findAll() {
        return inventoryOrderRepository.findAll();
    }

    public InventoryOrder findByOrderId(int id) {
        return inventoryOrderRepository.findByOrderId((id));
    }

    public void save(InventoryOrder inventoryOrder) {
         inventoryOrderRepository.save(inventoryOrder);
    }

    public void delete(int orderId) {
        inventoryOrderRepository.deleteById(orderId);
    }
}
