package gr1.fpt.bambikitchen.service;

import gr1.fpt.bambikitchen.model.InventoryTransaction;

import java.util.List;

public interface InventoryTransactionService {
    List<InventoryTransaction> findAll();
    InventoryTransaction findById(int id);
    InventoryTransaction save(InventoryTransaction transaction);
    InventoryTransaction update(InventoryTransaction transaction);
    void delete(int id);
}
