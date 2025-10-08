package gr1.fpt.bambikitchen.service;

import gr1.fpt.bambikitchen.model.Payment;
import gr1.fpt.bambikitchen.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    IngredientService ingredientService;

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public Payment getPaymentById(int id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Transactional
    public void paymentSucess (int paymentId, String paymentMethod, String transactionId){
        Payment payment = getPaymentById(paymentId);
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId(transactionId);
        payment.setStatus("Success");
        savePayment(payment);
        ingredientService.minusInventory(paymentId);
    }

    public void paymentFail (int paymentId, String paymentMethod){
        Payment payment = getPaymentById(paymentId);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("Fail");
        savePayment(payment);
        ingredientService.resetReserve(paymentId);
    }
}
