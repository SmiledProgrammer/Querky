package pl.szinton.querky.service.play;

import pl.szinton.querky.message.EventMessage;

public interface IWordsSoloService {

    EventMessage getRandomWordId();

    EventMessage makeGuess(int wordId, String guessWord);
}
