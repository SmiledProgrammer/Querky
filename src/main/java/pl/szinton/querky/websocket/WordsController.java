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

    protected static final String DATA_INT_PARSE_EXCEPTION_MESSAGE =
            "Failed to parse WebSockets message content - first argument must by a number.";

    protected final WordsService wordsService;
    protected final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/words/join-table")
    public void handleJoinedTable(@Header("simpSessionId") String sessionId, List<String> dataMsg) {
        String username = "" + sessionId; // TODO: fetch from user
        int tableNumber = StringParsingUtils.toInt(dataMsg.get(0), DATA_INT_PARSE_EXCEPTION_MESSAGE);
        EventMessage response = wordsService.joinTable(username, tableNumber);
        messagingTemplate.convertAndSend("/queue/direct-" + username, response);
        if (!response.isError()) {
            EventMessage broadcast = wordsService.broadcastJoinedTable(username);
            messagingTemplate.convertAndSend("/topic/table-" + tableNumber, broadcast);
        }
    }

    @MessageMapping("/words/leave-table")
    public void handleLeftTable(@Header("simpSessionId") String sessionId) {
        String username = "" + sessionId; // TODO: fetch from user
        int tableNumber = wordsService.getPlayersTableNumber(username);
        boolean success = wordsService.leaveTable(username);
        if (success) {
            EventMessage broadcast = wordsService.broadcastLeftTable(username);
            messagingTemplate.convertAndSend("/topic/table-" + tableNumber, broadcast);
        }
    }
}
