package pl.szinton.querky.game.words;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.szinton.querky.service.play.WordsService;

@Component
@RequiredArgsConstructor
public class BattlesGameTimer {

    public final WordsService wordsService;

    @Async
    @Scheduled(fixedRate = 1000)
    public void startGameTimer() {
        wordsService.handleTimerTick();
    }
}
