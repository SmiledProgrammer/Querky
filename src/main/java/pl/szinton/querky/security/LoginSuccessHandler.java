package pl.szinton.querky.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.szinton.querky.service.rest.UserService;
import pl.szinton.querky.utils.OAuth2Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final SessionManager sessionManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);
        String principalName = authentication.getName();
        String email = OAuth2Utils.getUserEmail(authentication);
        sessionManager.createSession(principalName, email);
        if (userService.userExists(email)) {
            userService.createUser(email);
        }
    }
}
