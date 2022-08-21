package pl.szinton.querky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class QuerkyApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuerkyApplication.class, args);
    }
}
