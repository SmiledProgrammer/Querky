package pl.szinton.querky.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class OAuth2Utils {

    public static String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return getUserEmail(auth);
    }

    public static String getUserEmail(Authentication authentication) {
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        Object emailAttribute = user.getAttributes().get("email");
        return String.valueOf(emailAttribute);
    }
}
