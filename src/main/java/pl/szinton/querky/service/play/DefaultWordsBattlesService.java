package pl.szinton.querky.service.play;

import org.springframework.stereotype.Service;
import pl.szinton.querky.enums.WordsEvent;
import pl.szinton.querky.enums.WordsGameState;
import pl.szinton.querky.game.words.BattlesGame;
import pl.szinton.querky.game.words.IBattlesGame;
import pl.szinton.querky.game.words.LetterMatch;
import pl.szinton.querky.game.words.Player;
import pl.szinton.querky.message.EventMessage;
import pl.szinton.querky.service.rest.IWordsDictionaryService;
import pl.szinton.querky.websocket.IOutputController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultWordsBattlesService implements IWordsBattlesService {

    protected static final int TABLES_COUNT = 1;

    protected final IOutputController outputController;
    protected final IWordsDictionaryService dictionaryService;
    protected final Map<String, Integer> playersOnTables;
    protected final Map<Integer, IBattlesGame> gameTables;

    public DefaultWordsBattlesService(IOutputController outputController, IWordsDictionaryService dictionaryService) {
        this.outputController = outputController;
        this.dictionaryService = dictionaryService;
        this.playersOnTables = new HashMap<>();
        this.gameTables = new HashMap<>();
        for (int i = 0; i < TABLES_COUNT; i++) {
            int tableNumber = 100 + i;
            IBattlesGame newGame = new BattlesGame(tableNumber, this, dictionaryService);
            this.gameTables.put(tableNumber, newGame);
        }
    }

    @Override
    public Integer getPlayersTableNumber(String username) {
        return playersOnTables.get(username);
    }

    @Override
    public EventMessage joinTable(String username, int tableNumber) {
        if (!gameTables.containsKey(tableNumber)) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_NO_SUCH_TABLE);
        }
        IBattlesGame table = gameTables.get(tableNumber);
        if (table.hasPlayer(username)) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_PLAYER_ALREADY_ON_TABLE);
        }
        if (table.hasReachedPlayersLimit()) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_TABLE_FULL);
        }
        table.addPlayer(username);
        playersOnTables.put(username, tableNumber);
        List<Player> playerList = table.getPlayersList();
        int joiningPlayerIndex = getIndexOfPlayer(playerList, username);
        return EventMessage.fromWordsEvent(WordsEvent.TABLE_DATA, tableNumber, table.getGameStartTimeLeft(),
                table.getRoundsLeft(), table.getRoundTimeLeft(), table.getGameState(), playerList, joiningPlayerIndex);
    }

    @Override
    public EventMessage leaveTable(String username) {
        Integer tableNumber = getPlayersTableNumber(username);
        if (tableNumber == null) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_PLAYER_NOT_ON_TABLE);
        }
        IBattlesGame table = gameTables.get(tableNumber);
        table.removePlayer(username);
        playersOnTables.remove(username);
        return EventMessage.fromWordsEvent(WordsEvent.PLAYER_LEFT_TABLE, username);
    }

    @Override
    public EventMessage makeGuess(String username, String guessWord) {
        if (dictionaryService.doesNotContainWord(guessWord)) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_DISALLOWED_WORD);
        }
        Integer tableNumber = getPlayersTableNumber(username);
        if (tableNumber == null) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_PLAYER_NOT_ON_TABLE);
        }
        IBattlesGame game = gameTables.get(tableNumber);
        if (game.getGameState() != WordsGameState.GUESSING_PHASE) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_NOT_GUESSING_PHASE);
        }
        if (game.playerDoesNotHaveGuessesLeft(username)) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_PLAYER_HAS_NO_GUESSES_LEFT);
        }
        if (game.playerHasAlreadyGuessedCurrentWord(username)) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_PLAYER_HAS_ALREADY_GUESSED);
        }
        LetterMatch match = game.processPlayerGuess(username, guessWord);
        return EventMessage.fromWordsEvent(WordsEvent.PLAYER_GUESS, username, match);
    }

    @Override
    public void checkIfAllPlayersOnTableFinished(int tableNumber) {
        IBattlesGame game = gameTables.get(tableNumber);
        game.checkIfAllPlayersFinished();
    }

    @Override
    public EventMessage broadcastJoinedTable(String username) {
        return EventMessage.fromWordsEvent(WordsEvent.PLAYER_JOINED_TABLE, username);
    }

    @Override
    public void handleRoundStart(int tableNumber) {
        EventMessage msg = EventMessage.fromWordsEvent(WordsEvent.ROUND_COUNTDOWN_START);
        outputController.broadcastTableMessage(tableNumber, msg);
    }

    @Override
    public void handleGuessingPhaseStart(int tableNumber) {
        EventMessage msg = EventMessage.fromWordsEvent(WordsEvent.ROUND_GUESSING_PHASE_START);
        outputController.broadcastTableMessage(tableNumber, msg);
    }

    @Override
    public void handleRoundEnd(int tableNumber, Map<String, Integer> playerPoints) {
        EventMessage msg = EventMessage.fromWordsEvent(WordsEvent.ROUND_END, playerPoints);
        outputController.broadcastTableMessage(tableNumber, msg);
    }

    @Override
    public void handleTimerTick() {
        for (IBattlesGame table : gameTables.values()) {
            table.handleTimerTick();
        }
    }

    private int getIndexOfPlayer(List<Player> playerList, String username) {
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            if (username.equals(player.getUsername())) {
                return i;
            }
        }
        return -1;
    }
}
