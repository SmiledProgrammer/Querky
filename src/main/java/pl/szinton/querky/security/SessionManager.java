package pl.szinton.querky.security;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.szinton.querky.utils.OAuth2Utils;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public final class SessionManager {

    private final Map<String, Session> sessions;

    public SessionManager() {
        sessions = new HashMap<>();
    }

    public void createSession(String principalName, String email) {
        int atSignIndex = email.indexOf("@");
        String username = email.substring(0, atSignIndex);
        Session session = new Session(email, username);
        sessions.put(principalName, session);
        log.info("Created session with ID: {}", principalName);
    }

    // TODO: run automatically for sessions older than 15 minutes (+auto logout)
    public void terminateSession(Principal principal) {
        String principalName = principal.getName();
        sessions.remove(principalName);
    }

    public void setPrincipalWebSocketSessionId(Principal principal, String sessionId) {
        String principalName = principal.getName();
        sessions.get(principalName).setWebSocketSessionId(sessionId);
    }

    public String getPrincipalUsername(Principal principal) {
        String principalName = principal.getName();
        return sessions.get(principalName).getUsername();
    }

    public String getPrincipalWebSocketSessionId(Principal principal) {
        String principalName = principal.getName();
        return sessions.get(principalName).getWebSocketSessionId();
    }

    @Getter
    @Setter
    private static class Session {

        private final String email;
        private final String username;
        private String webSocketSessionId;

        public Session(String email, String username) {
            this.email = email;
            this.username = username;
        }
    }
}
