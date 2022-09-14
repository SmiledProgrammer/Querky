package pl.szinton.querky.service.play;

import org.springframework.stereotype.Service;
import pl.szinton.querky.enums.WordsEvent;
import pl.szinton.querky.game.words.BattlesGame;
import pl.szinton.querky.message.EventMessage;
import pl.szinton.querky.service.rest.WordsDictionaryService;
import pl.szinton.querky.websocket.WordsOutputController;

import java.util.HashMap;
import java.util.Map;

@Service
public class WordsService {

    protected static final int TABLES_COUNT = 1;

    protected final WordsOutputController outputController;
    protected final Map<String, Integer> playersOnTables;
    protected final Map<Integer, BattlesGame> gameTables;

    public WordsService(WordsOutputController outputController, WordsDictionaryService dictionaryService) {
        this.outputController = outputController;
        this.playersOnTables = new HashMap<>();
        this.gameTables = new HashMap<>();
        BattlesGame.initServices(this, dictionaryService);
        for (int i = 0; i < TABLES_COUNT; i++) {
            int tableNumber = 100 + i;
            BattlesGame newGame = new BattlesGame(tableNumber);
            this.gameTables.put(tableNumber, newGame);
        }
    }

    public int getPlayersTableNumber(String username) {
        return playersOnTables.get(username);
    }

    public EventMessage joinTable(String username, int tableNumber) {
        if (!gameTables.containsKey(tableNumber)) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_NO_SUCH_TABLE);
        }
        BattlesGame table = gameTables.get(tableNumber);
        if (table.hasPlayer(username)) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_PLAYER_ALREADY_ON_TABLE);
        }
        if (table.hasReachedPlayersLimit()) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_TABLE_FULL);
        }
        table.addPlayer(username);
        playersOnTables.put(username, tableNumber);
        return EventMessage.fromWordsEvent(WordsEvent.TABLE_DATA, tableNumber, table.getGameStartTimeLeft(),
                table.getRoundsLeft(), table.getRoundTimeLeft(), table.getGameState(), table.getPlayersList());
    }

    public boolean leaveTable(String username) {
        int tableNumber = getPlayersTableNumber(username);
        BattlesGame table = gameTables.get(tableNumber);
        if (!table.hasPlayer(username)) {
            return false;
        }
        table.removePlayer(username);
        playersOnTables.remove(username);
        return true;
    }

    public EventMessage broadcastJoinedTable(String username) {
        return EventMessage.fromWordsEvent(WordsEvent.PLAYER_JOINED_TABLE, username);
    }

    public EventMessage broadcastLeftTable(String username) {
        return EventMessage.fromWordsEvent(WordsEvent.PLAYER_LEFT_TABLE, username);
    }

    public void handleRoundStart(int tableNumber) {
        EventMessage msg = EventMessage.fromWordsEvent(WordsEvent.ROUND_COUNTDOWN_START);
        outputController.broadcastTableMessage(tableNumber, msg);
    }

    public void handleGuessingPhaseStart(int tableNumber) {
        EventMessage msg = EventMessage.fromWordsEvent(WordsEvent.ROUND_GUESSING_PHASE_START);
        outputController.broadcastTableMessage(tableNumber, msg);
    }

    public void handleRoundEnd(int tableNumber, Map<String, Integer> playerPoints) {
        EventMessage msg = EventMessage.fromWordsEvent(WordsEvent.ROUND_END, playerPoints);
        outputController.broadcastTableMessage(tableNumber, msg);
    }

    public void handleTimerTick() {
        for (BattlesGame table : gameTables.values()) {
            table.handleTimerTick();
        }
    }
}
