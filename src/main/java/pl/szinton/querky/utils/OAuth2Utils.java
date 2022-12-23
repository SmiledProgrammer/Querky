package pl.szinton.querky.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import pl.szinton.querky.exception.OAuth2DataFetchingException;

import java.util.List;

public class OAuth2Utils {

    public static String getUserEmail(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticatedPrincipal principal) {
            Object emailAttribute = principal.getAttributes().get("email");
            return String.valueOf(emailAttribute);
        } else if (authentication instanceof OAuth2AuthenticationToken token) {
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) token.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if ("ROLE_USER".equals(authority.getAuthority())) {
                    OAuth2UserAuthority oAuth2Authority = (OAuth2UserAuthority) authority;
                    Object emailAttribute = oAuth2Authority.getAttributes().get("email");
                    return String.valueOf(emailAttribute);
                }
            }
        }
        throw new OAuth2DataFetchingException("Couldn't fetch email from OAuth2 token.");
    }
}
