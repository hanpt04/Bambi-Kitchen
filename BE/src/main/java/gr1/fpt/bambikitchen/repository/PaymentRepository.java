package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findAllByAccountId(int accountId);
}
