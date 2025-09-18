package gr1.fpt.bambikitchen.Payment;

import org.springframework.stereotype.Component;

@Component("MOMO")
public class Momo implements PaymentMethod {

    @Override
    public void pay(double amount) {
        System.out.println( "Momo :" + amount );
    }
}
