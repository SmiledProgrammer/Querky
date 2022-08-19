package pl.szinton.querky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szinton.querky.utils.OAuth2Utils;

@SpringBootApplication
@RestController
public class QuerkyApplication {

    @GetMapping("/email")
    public String getEmail() {
        return OAuth2Utils.getCurrentUserEmail();
    }

    public static void main(String[] args) {
        SpringApplication.run(QuerkyApplication.class, args);
    }
}
