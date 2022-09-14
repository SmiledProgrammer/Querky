package pl.szinton.querky.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pl.szinton.querky.message.EventMessage;

@Component
@RequiredArgsConstructor
public class WordsOutputController {

    protected final SimpMessagingTemplate messagingTemplate;

    public void broadcastTableMessage(int tableNumber, EventMessage broadcastMsg) {
        messagingTemplate.convertAndSend("/topic/table-" + tableNumber, broadcastMsg);
    }

    public void sendDirectMessage(String username, EventMessage directMsg) {
        messagingTemplate.convertAndSend("/queue/direct-" + username, directMsg);
    }
}
