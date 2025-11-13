package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.event.EventListenerSystem;
import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.*;
import gr1.fpt.bambikitchen.model.enums.OrderStatus;
import gr1.fpt.bambikitchen.repository.*;
import gr1.fpt.bambikitchen.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    IngredientService ingredientService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private NutritionRepository nutritionRepository;
    @Autowired
    private EventListenerSystem eventListenerSystem;


    public Payment savePayment(Payment payment) {
      Payment saved = paymentRepository.save(payment);
      return saved;
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

        // Event task không block cái hàm này, không block payment
        applicationEventPublisher.publishEvent(new EventListenerSystem.MailDetails(
                accountRepository.findById(order.getUserId())
                        .orElseThrow()
                        .getMail(),
                order.getId()
        ));
    }

    public List<Payment> getAllByAccount(int accountId) {
        return paymentRepository.findAllByAccountId(accountId);
    }

    public List<Payment> getAll(){
        return paymentRepository.findAllByStatus("SUCCESS");
    }
}
