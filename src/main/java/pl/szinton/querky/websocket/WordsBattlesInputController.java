package pl.szinton.querky.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pl.szinton.querky.message.EventMessage;
import pl.szinton.querky.service.play.WordsBattlesService;
import pl.szinton.querky.utils.StringParsingUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WordsBattlesInputController {

    protected final WordsOutputController outputController;
    protected final WordsBattlesService wordsService;

    @MessageMapping("/words/join-table")
    public void handleJoinedTable(@Header("simpSessionId") String sessionId, List<String> dataMsg) {
        String username = "" + sessionId; // TODO: fetch from user
        int tableNumber = StringParsingUtils.toInt(dataMsg.get(0));
        EventMessage response = wordsService.joinTable(username, tableNumber);
        outputController.sendDirectMessage(username, response);
        if (!response.isError()) {
            EventMessage broadcast = wordsService.broadcastJoinedTable(username);
            outputController.broadcastTableMessage(tableNumber, broadcast);
        }
    }

    @MessageMapping("/words/leave-table")
    public void handleLeftTable(@Header("simpSessionId") String sessionId) {
        String username = "" + sessionId; // TODO: fetch from user
        EventMessage msg = wordsService.leaveTable(username);
        if (msg.isError()) {
            outputController.sendDirectMessage(username, msg);
            return;
        }
        int tableNumber = wordsService.getPlayersTableNumber(username);
        outputController.broadcastTableMessage(tableNumber, msg);
    }

    @MessageMapping("/words/guess")
    public void handlePlayerGuess(@Header("simpSessionId") String sessionId, List<String> dataMsg) {
        String username = "" + sessionId; // TODO: fetch from user
        String guessWord = dataMsg.get(0);
        EventMessage msg = wordsService.makeGuess(username, guessWord);
        if (msg.isError()) {
            outputController.sendDirectMessage(username, msg);
            return;
        }
        int tableNumber = wordsService.getPlayersTableNumber(username);
        outputController.broadcastTableMessage(tableNumber, msg);
        wordsService.checkIfAllPlayersOnTableFinished(tableNumber);
    }
}
