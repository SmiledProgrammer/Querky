package pl.szinton.querky.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.szinton.querky.service.game.WordsService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/words")
public class WordsController {

    private final WordsService wordsService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/join-table")
    private void sendTableData(@Header("simpSessionId") String sessionId) {
        String tableData = "Epic table data";
        messagingTemplate.convertAndSend("/queue/direct-" + sessionId, tableData);
    }
}
