package pl.szinton.querky.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pl.szinton.querky.message.EventMessage;
import pl.szinton.querky.security.SessionManager;
import pl.szinton.querky.service.play.IWordsBattlesService;
import pl.szinton.querky.utils.StringParsingUtils;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WordsBattlesInputController implements IWordsBattlesInputController {

    protected final IOutputController outputController;
    protected final IWordsBattlesService wordsService;
    protected final SessionManager sessionManager;

    @Override
    @MessageMapping("/words/join-table")
    public void handleJoinedTable(@Header("simpSessionId") String sessionId,
                                  Principal principal, List<String> dataMsg) {
        String username = sessionManager.getPrincipalUsername(principal);
        sessionManager.setPrincipalWebSocketSessionId(principal, sessionId);
        int tableNumber = StringParsingUtils.toInt(dataMsg.get(0));
        EventMessage msg = wordsService.joinTable(username, tableNumber);
        outputController.sendDirectMessage(principal, msg);
        if (!msg.isError()) {
            EventMessage broadcast = wordsService.broadcastJoinedTable(username);
            outputController.broadcastTableMessage(tableNumber, broadcast);
        }
    }

    @Override
    @MessageMapping("/words/leave-table")
    public void handleLeftTable(Principal principal) {
        String username = sessionManager.getPrincipalUsername(principal);
        EventMessage msg = wordsService.leaveTable(username);
        if (msg.isError()) {
            outputController.sendDirectMessage(principal, msg);
            return;
        }
        int tableNumber = wordsService.getPlayersTableNumber(username);
        outputController.broadcastTableMessage(tableNumber, msg);
    }

    @Override
    @MessageMapping("/words/guess")
    public void handlePlayerGuess(Principal principal, List<String> dataMsg) {
        String username = sessionManager.getPrincipalUsername(principal);
        String guessWord = dataMsg.get(0);
        EventMessage msg = wordsService.makeGuess(username, guessWord);
        if (msg.isError()) {
            outputController.sendDirectMessage(principal, msg);
            return;
        }
        int tableNumber = wordsService.getPlayersTableNumber(username);
        outputController.broadcastTableMessage(tableNumber, msg);
        wordsService.checkIfAllPlayersOnTableFinished(tableNumber);
    }
}
