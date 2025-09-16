package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.InventoryTransaction;
import gr1.fpt.bambikitchen.repository.InventoryTransactionRepository;
import gr1.fpt.bambikitchen.service.InventoryTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    @Autowired
    private InventoryTransactionRepository repo;

    @Override
    public List<InventoryTransaction> findAll() {
        return repo.findAll();
    }

    @Override
    public InventoryTransaction findById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Inventory transaction not found!", HttpStatus.NOT_FOUND));
    }

    @Override
    public InventoryTransaction save(InventoryTransaction transaction) {
        return repo.save(transaction);
    }

    @Override
    public InventoryTransaction update(InventoryTransaction transaction) {
        if (repo.existsById(transaction.getId())) {
            return repo.save(transaction);
        } else {
            throw new CustomException("Inventory transaction not found!", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(int id) {
        if (!repo.existsById(id)) {
            throw new CustomException("Inventory transaction not found!", HttpStatus.NOT_FOUND);
        }
        repo.deleteById(id);
    }

}
