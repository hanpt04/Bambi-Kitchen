package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.InventoryTransaction;
import gr1.fpt.bambikitchen.service.InventoryTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-transaction")
@CrossOrigin(origins = "*")
public class InventoryTransactionController {

    @Autowired
    private InventoryTransactionService service;

    @GetMapping
    public ResponseEntity<List<InventoryTransaction>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryTransaction> findById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<InventoryTransaction> save(@RequestBody InventoryTransaction transaction) {
        return ResponseEntity.ok(service.save(transaction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryTransaction> update(@PathVariable int id, @RequestBody InventoryTransaction transaction) {
        transaction.setId(id);
        return ResponseEntity.ok(service.update(transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
