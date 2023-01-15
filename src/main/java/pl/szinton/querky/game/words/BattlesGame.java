package pl.szinton.querky.game.words;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.szinton.querky.enums.WordsGameState;
import pl.szinton.querky.service.play.IWordsBattlesService;
import pl.szinton.querky.service.rest.IWordsDictionaryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.szinton.querky.game.words.Constants.*;

@Slf4j
@Getter
public class BattlesGame implements IBattlesGame {

    protected IWordsBattlesService wordsService;
    protected IWordsDictionaryService dictionaryService;

    protected final int tableNumber;
    protected WordsGameState gameState;
    protected String currentWord;
    protected int gameStartTimeLeft;
    protected int roundsLeft;
    protected int roundTimeLeft;
    protected final Map<String, Player> players;
    protected final List<String> orderOfCorrectGuesses;

    public BattlesGame(int tableNumber, IWordsBattlesService wordsService, IWordsDictionaryService dictionaryService) {
        this.wordsService = wordsService;
        this.dictionaryService = dictionaryService;
        this.tableNumber = tableNumber;
        this.gameState = WordsGameState.WAITING_FOR_PLAYERS;
        this.currentWord = null;
        this.gameStartTimeLeft = GAME_START_COUNTDOWN_DURATION;
        this.roundsLeft = ROUND_COUNT;
        this.roundTimeLeft = ROUND_START_COUNTDOWN_DURATION + ROUND_DURATION;
        this.players = new HashMap<>();
        this.orderOfCorrectGuesses = new ArrayList<>();
    }

    @Override
    public synchronized void addPlayer(String username) {
        log.info("Player {} joined the table.", username);
        if (!hasPlayer(username) && !hasReachedPlayersLimit()) {
            Player newPlayer = new Player(username);
            newPlayer.setIsPlaying(true);
            players.put(username, newPlayer);
            if (players.size() >= 2) {
                initGameStartCountdown();
            }
        }
    }

    @Override
    public synchronized void removePlayer(String username) {
        log.info("Player {} left the table.", username);
        players.remove(username);
        if (players.size() <= 1) {
            if (gameState == WordsGameState.GAME_START_COUNTDOWN) {
                cancelGameStartCountdown();
            } else if (gameState != WordsGameState.WAITING_FOR_PLAYERS) {
                endGame();
            }
        }
    }

    private synchronized void startGame() {
        log.info("Started game.");
        for (Player player : players.values()) {
            player.resetPoints();
            player.resetLetterMatches();
        }
        roundsLeft = ROUND_COUNT;
        startRoundCountdown();
    }

    private synchronized void startRoundCountdown() {
        if (roundsLeft > 0) {
            log.info("Starting round...");
            gameState = WordsGameState.ROUND_START_COUNTDOWN;
            currentWord = dictionaryService.getRandomWordToGuess();
            roundTimeLeft = ROUND_START_COUNTDOWN_DURATION + ROUND_DURATION;
            wordsService.handleRoundStart(tableNumber);
        } else {
            endGame();
        }
    }

    private synchronized void startGuessingPhase() {
        log.info("Started round number {}.", ROUND_COUNT - roundsLeft + 1);
        gameState = WordsGameState.GUESSING_PHASE;
        wordsService.handleGuessingPhaseStart(tableNumber);
    }

    private synchronized void endRound() {
        log.info("Ended round.");
        gameState = WordsGameState.ROUND_ENDING;
        roundsLeft -= 1;
        for (Player player : players.values()) {
            int guessPlace = orderOfCorrectGuesses.indexOf(player.getUsername());
            player.processPoints(guessPlace, players.size());
            player.resetLetterMatches();
        }
        orderOfCorrectGuesses.clear();
        Map<String, Integer> playerPoints = players.values().stream()
                .collect(Collectors.toMap(Player::getUsername, Player::getPoints));
        wordsService.handleRoundEnd(tableNumber, playerPoints);
        startRoundCountdown();
    }

    @Override
    public synchronized LetterMatch processPlayerGuess(String username, String guessWord) {
        if (dictionaryService.doesNotContainWord(guessWord)) {
            return null;
        }
        Player player = players.get(username);
        if (player.hasGuessedCorrectly() || !player.hasGuessesLeft()) {
            return null;
        }
        LetterMatch match = LetterMatch.checkWordMatching(currentWord, guessWord);
        boolean correctGuess = LetterMatch.isPerfectMatch(match);
        player.makeGuess(match, correctGuess);
        if (correctGuess) {
            orderOfCorrectGuesses.add(username);
        }
        // TODO: shorten round time left
        return match;
    }

    @Override
    public synchronized void checkIfAllPlayersFinished() {
        boolean allFinished = true;
        if (orderOfCorrectGuesses.size() != players.size()) {
            for (Player player : players.values()) {
                if (player.hasGuessesLeft()) {
                    allFinished = false;
                    break;
                }
            }
        }
        if (allFinished) {
            endRound();
        }
    }

    private synchronized void endGame() {
        log.info("Ended game.");
        gameState = WordsGameState.WAITING_FOR_PLAYERS;
        for (Player player : players.values()) {
            player.setIsPlaying(false);
        }
    }

    private synchronized void initGameStartCountdown() {
        log.info("Started game start countdown.");
        gameState = WordsGameState.GAME_START_COUNTDOWN;
        gameStartTimeLeft = GAME_START_COUNTDOWN_DURATION;
    }

    private synchronized void cancelGameStartCountdown() {
        log.info("Canceled game start countdown.");
        gameState = WordsGameState.WAITING_FOR_PLAYERS;
    }

    @Override
    public synchronized void handleTimerTick() {
        if (gameState == WordsGameState.GAME_START_COUNTDOWN) {
            log.info("Game start time: {}", gameStartTimeLeft);
            gameStartTimeLeft -= 1;
            if (gameStartTimeLeft < 0) {
                startGame();
            }
        } else if (gameState == WordsGameState.ROUND_START_COUNTDOWN ||
                gameState == WordsGameState.GUESSING_PHASE ||
                gameState == WordsGameState.ROUND_ENDING) {
            log.info("Round time: {}", roundTimeLeft);
            roundTimeLeft -= 1;
            if (roundTimeLeft == ROUND_DURATION) {
                startGuessingPhase();
            } else if (roundTimeLeft == 0) {
                endRound();
            }
        }
    }

    @Override
    public List<Player> getPlayersList() {
        return new ArrayList<>(players.values());
    }

    @Override
    public boolean hasPlayer(String username) {
        return players.containsKey(username);
    }

    @Override
    public boolean hasReachedPlayersLimit() {
        return players.size() == PLAYERS_LIMIT;
    }

    @Override
    public boolean playerDoesNotHaveGuessesLeft(String username) {
        return !players.get(username).hasGuessesLeft();
    }

    @Override
    public boolean playerHasAlreadyGuessedCurrentWord(String username) {
        return players.get(username).hasGuessedCorrectly();
    }
}
