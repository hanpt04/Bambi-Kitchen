package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.Factory.PaymentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    PaymentFactory paymentFactory;

    @GetMapping("/testpayment")
    public void testPayment(@RequestParam String paymentMethodName) {

        paymentFactory.getPaymentMethod(paymentMethodName).pay(20);
    }
}
