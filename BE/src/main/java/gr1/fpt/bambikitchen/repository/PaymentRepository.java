package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
