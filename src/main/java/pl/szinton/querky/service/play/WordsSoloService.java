package pl.szinton.querky.service.play;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szinton.querky.enums.WordsEvent;
import pl.szinton.querky.game.words.LetterMatch;
import pl.szinton.querky.message.EventMessage;
import pl.szinton.querky.service.rest.WordsDictionaryService;
import pl.szinton.querky.websocket.WordsOutputController;

@Service
@RequiredArgsConstructor
public class WordsSoloService {

    protected final WordsOutputController outputController;
    protected final WordsDictionaryService dictionaryService;

    public EventMessage getRandomWordId() {
        int wordId = dictionaryService.getRandomWordToGuessIndex();
        return EventMessage.fromWordsEvent(WordsEvent.RANDOM_WORD_RESPONSE, wordId);
    }

    public EventMessage makeGuess(int wordId, String guessWord) {
        if (dictionaryService.doesNotContainWord(guessWord)) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_DISALLOWED_WORD);
        }
        String wordToGuess = dictionaryService.getWordToGuessByIndex(wordId);
        if (wordToGuess == null) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_NO_WORD_WITH_THIS_INDEX);
        }
        LetterMatch match = LetterMatch.checkWordMatching(wordToGuess, guessWord);
        return EventMessage.fromWordsEvent(WordsEvent.PLAYER_GUESS, match);
    }
}
