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

        mailBillToUser(accountRepository.findById(order.getUserId())
                        .orElseThrow(() -> new CustomException("Email not found", HttpStatus.BAD_REQUEST))
                        .getMail(),
                order.getId());
    }

    @Async
    protected void mailBillToUser(String email, int orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrders_Id(orderId);
        applicationEventPublisher.publishEvent(new EventListenerSystem.SendOrderEvent(email, toOrderDetailsMap(orderDetails)));
    }

    private List<EventListenerSystem.DishInfo> toOrderDetailsMap(List<OrderDetail> orderDetails) {
        return orderDetails.parallelStream()
                        .flatMap(orderDetail -> Stream.of(orderDetail.getDish()))
                        .distinct()
                        .flatMap(dish -> Stream.of(
                                EventListenerSystem.DishInfo.builder()
                                        .name(dish.getName())
                                        .price(dish.getPrice())
                                        .ingredients(fromDishToIngredientsInfo(dish))
                                        .build()
                                )
                        ).collect(Collectors.toList());
    }

    private Map<String, Integer> fromDishToIngredientsInfo(Dish dish) {
        return recipeRepository.getIngredientsByDish_Id(dish.getId())
                .parallelStream()
                .collect(Collectors.toMap(
                        recipe -> recipe.getIngredient().getName(),
                        Recipe::getQuantity
                ));
    }


    public List<Payment> getAllByAccount(int accountId) {
        return paymentRepository.findAllByAccountId(accountId);
    }

    public List<Payment> getAll(){
        return paymentRepository.findAllByStatus("SUCCESS");
    }
}
