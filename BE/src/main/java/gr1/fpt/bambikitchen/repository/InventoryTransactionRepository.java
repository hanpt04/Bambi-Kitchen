package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Integer> {
}
