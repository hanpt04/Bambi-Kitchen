package gr1.fpt.bambikitchen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BambiKitchenApplication {

    public static void main(String[] args) {
        SpringApplication.run(BambiKitchenApplication.class, args);
    }

}
