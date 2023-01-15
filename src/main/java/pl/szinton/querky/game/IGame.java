package pl.szinton.querky.game;

import pl.szinton.querky.game.words.Player;

import java.util.List;

public interface IGame {

    void addPlayer(String username);

    void removePlayer(String username);

    List<Player> getPlayersList();

    boolean hasPlayer(String username);

    boolean hasReachedPlayersLimit();
}
