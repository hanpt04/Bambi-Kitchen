package gr1.fpt.bambikitchen.service;

import gr1.fpt.bambikitchen.model.Orders;
import gr1.fpt.bambikitchen.model.Payment;
import gr1.fpt.bambikitchen.model.enums.OrderStatus;
import gr1.fpt.bambikitchen.repository.OrderRepository;
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
    @Autowired
    OrderRepository orderRepository;

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
        payment.setStatus("SUCCESS");
        savePayment(payment);
        ingredientService.minusInventory(paymentId);

        getOrderByPaymentIdAndUpdateStatus(paymentId, OrderStatus.PAID);

    }

    public void paymentFail (int paymentId, String paymentMethod){
        Payment payment = getPaymentById(paymentId);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("FAIL");
        payment.setNote("Payment failed or cancelled by user");
        savePayment(payment);
        ingredientService.resetReserve(paymentId);
      getOrderByPaymentIdAndUpdateStatus(paymentId, OrderStatus.CANCELLED);
    }

    public void getOrderByPaymentIdAndUpdateStatus(int paymentId, OrderStatus status) {
        Orders order = orderRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        if (status == OrderStatus.CANCELLED) {
            order.setNote("PAYMENT FAILED");
        }
        orderRepository.save(order);

    }
}
