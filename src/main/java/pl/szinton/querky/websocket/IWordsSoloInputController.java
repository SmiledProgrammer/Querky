package pl.szinton.querky.websocket;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.security.Principal;
import java.util.List;

public interface IWordsSoloInputController {

    @MessageMapping("/words/solo/random-word")
    void handleRandomWordRequest(@Header("simpSessionId") String sessionId, Principal principal);

    @MessageMapping("/words/solo/guess")
    void handleSoloGuess(Principal principal, List<String> dataMsg);
}
