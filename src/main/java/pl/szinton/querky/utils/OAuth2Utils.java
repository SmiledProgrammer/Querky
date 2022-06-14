package pl.szinton.querky.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class OAuth2Utils {

    public static String getCurrentUserEmail() {
        DefaultOAuth2User user = (DefaultOAuth2User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Object emailAttribute = user.getAttributes().get("email");
        return String.valueOf(emailAttribute);
    }
}
