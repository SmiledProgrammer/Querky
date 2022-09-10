package pl.szinton.querky.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import pl.szinton.querky.message.EventMessage;
import pl.szinton.querky.service.play.WordsService;
import pl.szinton.querky.utils.StringParsingUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j // TODO: remove
public class WordsController {

    private final WordsService wordsService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/words/join-table")
    public void handleJoinTable(@Header("simpSessionId") String sessionId, List<String> dataMsg) {
        String username = "" + sessionId; // TODO: fetch from user
        int tableNumber = StringParsingUtils.toInt(dataMsg.get(0),
                "Failed to parse WebSockets message content - first argument must by a number.");
        EventMessage response = wordsService.joinTable(username, tableNumber);
        if (response.isValid()) {
            messagingTemplate.convertAndSend("/queue/direct-" + sessionId, response);
        }
    }
}
