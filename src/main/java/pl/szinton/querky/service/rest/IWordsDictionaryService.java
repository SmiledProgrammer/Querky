package pl.szinton.querky.service.rest;

public interface IWordsDictionaryService {

    boolean doesNotContainWord(String guessEntryWord);

    String getRandomWordToGuess();

    int getRandomWordToGuessIndex();

    String getWordToGuessByIndex(int wordIndex);
}
