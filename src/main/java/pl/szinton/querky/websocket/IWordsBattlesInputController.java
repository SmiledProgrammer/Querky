package pl.szinton.querky.websocket;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.security.Principal;
import java.util.List;

public interface IWordsBattlesInputController {

    @MessageMapping("/words/join-table")
    void handleJoinedTable(@Header("simpSessionId") String sessionId,
                           Principal principal, List<String> dataMsg);

    @MessageMapping("/words/leave-table")
    void handleLeftTable(Principal principal);

    @MessageMapping("/words/guess")
    void handlePlayerGuess(Principal principal, List<String> dataMsg);
}
