package pl.szinton.querky.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pl.szinton.querky.message.EventMessage;
import pl.szinton.querky.service.play.WordsSoloService;
import pl.szinton.querky.utils.StringParsingUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WordsSoloInputController {

    protected final WordsOutputController outputController;
    protected final WordsSoloService wordsSoloService;

    @MessageMapping("/words/solo/random-word")
    public void handleRandomWordRequest(@Header("simpSessionId") String sessionId) {
        String username = "" + sessionId; // TODO: fetch from user
        EventMessage msg = wordsSoloService.getRandomWordId();
        outputController.sendDirectMessage(username, msg);
    }

    @MessageMapping("/words/solo/guess")
    public void handleSoloGuess(@Header("simpSessionId") String sessionId, List<String> dataMsg) {
        String username = "" + sessionId; // TODO: fetch from user
        int wordId = StringParsingUtils.toInt(dataMsg.get(0));
        String guessWord = dataMsg.get(1);
        EventMessage msg = wordsSoloService.makeGuess(wordId, guessWord);
        outputController.sendDirectMessage(username, msg);
    }
}
