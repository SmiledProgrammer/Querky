package pl.szinton.querky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class QuerkyApplication {

    @GetMapping("/email")
    public String getEmail() {
        DefaultOAuth2User user = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return String.valueOf(user.getAttributes().get("email"));
    }

    public static void main(String[] args) {
        SpringApplication.run(QuerkyApplication.class, args);
    }
}
