package gr1.fpt.bambikitchen.Payment;

import org.springframework.stereotype.Component;

@Component("VNPAY")
public class VNPAY implements PaymentMethod {

    @Override
    public void pay(double amount) {
        System.out.println( "VNPAY :" + amount );
    }

    @Override
    public String createPaymentRequest(long amount, int orderIdreal) throws Exception {
        return "";
    }


}
