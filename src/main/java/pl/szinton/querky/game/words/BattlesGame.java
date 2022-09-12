package pl.szinton.querky.game.words;

import lombok.Getter;
import pl.szinton.querky.enums.WordsGameState;
import pl.szinton.querky.enums.WordsLetterMatch;
import pl.szinton.querky.service.rest.WordsDictionaryService;
import pl.szinton.querky.utils.ListUtils;

import java.util.*;
import java.util.stream.Collectors;

import static pl.szinton.querky.game.words.Constants.*;

@Getter
public class BattlesGame {

    private final WordsDictionaryService dictionaryService;

    protected int gameStartTimeLeft;
    protected int roundsLeft;
    protected int roundTimeLeft;
    protected WordsGameState gameState;
    protected final Map<String, Player> players;
    protected String currentWord;

    public BattlesGame(WordsDictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
        this.gameStartTimeLeft = GAME_START_COUNTDOWN_DURATION;
        this.roundsLeft = ROUND_COUNT;
        this.roundTimeLeft = ROUND_START_COUNTDOWN_DURATION + ROUND_DURATION;
        this.gameState = WordsGameState.WAITING_FOR_PLAYERS;
        this.players = new HashMap<>();
        this.currentWord = null;
    }

    public void addPlayer(String username) {
        if (!hasPlayer(username) && !hasReachedPlayersLimit()) {
            Player newPlayer = new Player(username);
            newPlayer.setIsPlaying(true);
            players.put(username, newPlayer);
        }
    }

    public void removePlayer(String username) {
        players.remove(username);
    }

    public boolean hasPlayer(String username) {
        return players.containsKey(username);
    }

    public void startNextRound() {
        currentWord = dictionaryService.getRandomWord();
        // TODO
    }

    public boolean isAllowedWord(String guessedWord) {
        return dictionaryService.containsGuessEntryWord(guessedWord);
    }

    public LetterMatch checkMatchingLetters(String guessedWord) {
        WordsLetterMatch[] matches = new WordsLetterMatch[WORD_LENGTH];
        List<Character> charsLeftToProcess = currentWord.chars()
                .mapToObj(c -> (char) c).collect(Collectors.toCollection(ArrayList::new));
        List<Boolean> indexLeftToProcess = ListUtils.createAndInitList(WORD_LENGTH, true);
        for (int i = 0; i < WORD_LENGTH; i++) {
            char currentWordLetter = currentWord.charAt(i);
            char guessedWordLetter = guessedWord.charAt(i);
            if (guessedWordLetter == currentWordLetter) {
                matches[i] = WordsLetterMatch.LETTER_CORRECT;
                charsLeftToProcess.remove(Character.valueOf(guessedWordLetter));
                indexLeftToProcess.set(i, false);
            }
        }
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (!indexLeftToProcess.get(i)) {
                continue;
            }
            char guessedWordLetter = guessedWord.charAt(i);
            if (charsLeftToProcess.contains(guessedWordLetter)) {
                matches[i] = WordsLetterMatch.LETTER_MISPLACED;
            } else {
                matches[i] = WordsLetterMatch.LETTER_NOT_IN_WORD;
            }
            charsLeftToProcess.remove(Character.valueOf(guessedWordLetter));
        }
        return new LetterMatch(matches);
    }

    public boolean hasReachedPlayersLimit() {
        return players.size() == PLAYERS_LIMIT;
    }

    public List<Player> getPlayersList() {
        return new ArrayList<>(players.values());
    }
}
