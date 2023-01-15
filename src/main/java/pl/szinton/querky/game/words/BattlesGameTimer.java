package pl.szinton.querky.game.words;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.szinton.querky.service.play.IWordsBattlesService;

@Component
@RequiredArgsConstructor
public class BattlesGameTimer {

    private final IWordsBattlesService wordsService;

    @Async
    @Scheduled(fixedRate = 1000)
    public void startGameTimer() {
        wordsService.handleTimerTick();
    }
}
