package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Integer> {
}
