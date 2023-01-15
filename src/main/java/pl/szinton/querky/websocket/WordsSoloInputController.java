package pl.szinton.querky.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pl.szinton.querky.message.EventMessage;
import pl.szinton.querky.security.SessionManager;
import pl.szinton.querky.service.play.IWordsSoloService;
import pl.szinton.querky.utils.StringParsingUtils;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WordsSoloInputController implements IWordsSoloInputController {

    protected final IOutputController outputController;
    protected final IWordsSoloService wordsSoloService;
    protected final SessionManager sessionManager;

    @Override
    @MessageMapping("/words/solo/random-word")
    public void handleRandomWordRequest(@Header("simpSessionId") String sessionId, Principal principal) {
        sessionManager.setPrincipalWebSocketSessionId(principal, sessionId);
        EventMessage msg = wordsSoloService.getRandomWordId();
        outputController.sendDirectMessage(principal, msg);
    }

    @Override
    @MessageMapping("/words/solo/guess")
    public void handleSoloGuess(Principal principal, List<String> dataMsg) {
        int wordId = StringParsingUtils.toInt(dataMsg.get(0));
        String guessWord = dataMsg.get(1);
        EventMessage msg = wordsSoloService.makeGuess(wordId, guessWord);
        outputController.sendDirectMessage(principal, msg);
    }
}
