package pl.szinton.querky.service.play;

import pl.szinton.querky.message.EventMessage;

import java.util.Map;

public interface IWordsBattlesService {

    Integer getPlayersTableNumber(String username);

    EventMessage joinTable(String username, int tableNumber);

    EventMessage leaveTable(String username);

    EventMessage makeGuess(String username, String guessWord);

    void checkIfAllPlayersOnTableFinished(int tableNumber);

    EventMessage broadcastJoinedTable(String username);

    void handleRoundStart(int tableNumber);

    void handleGuessingPhaseStart(int tableNumber);

    void handleRoundEnd(int tableNumber, Map<String, Integer> playerPoints);

    void handleTimerTick();
}
