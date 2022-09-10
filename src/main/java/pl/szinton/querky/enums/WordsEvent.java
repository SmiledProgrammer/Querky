package pl.szinton.querky.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WordsEvent {
    TABLE_DATA(101),
    PLAYER_JOINED_TABLE(102),
    PLAYER_LEFT_TABLE(103),
    PLAYER_READY(105),
    GAME_START(111),
    PLAYER_GUESS(221),
    ROUND_END(222),

    ERROR_NO_SUCH_TABLE(911);

    private final int code;
}
