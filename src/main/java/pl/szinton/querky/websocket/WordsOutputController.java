package pl.szinton.querky.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pl.szinton.querky.message.EventMessage;
import pl.szinton.querky.security.SessionManager;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class WordsOutputController implements IOutputController {

    protected final SimpMessagingTemplate messagingTemplate;
    protected final SessionManager sessionManager;

    @Override
    public void broadcastTableMessage(int tableNumber, EventMessage broadcastMsg) {
        messagingTemplate.convertAndSend("/topic/table-" + tableNumber, broadcastMsg);
    }

    @Override
    public void sendDirectMessage(Principal principal, EventMessage directMsg) {
        String sessionId = sessionManager.getPrincipalWebSocketSessionId(principal);
        messagingTemplate.convertAndSend("/queue/direct-" + sessionId, directMsg);
    }
}
