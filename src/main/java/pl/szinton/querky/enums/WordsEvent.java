package pl.szinton.querky.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum WordsEvent {
    TABLE_DATA(101),
    PLAYER_JOINED_TABLE(102),
    PLAYER_LEFT_TABLE(103),
    PLAYER_READY(105),
    GAME_START(111),
    PLAYER_GUESS(221),
    ROUND_END(222),

    ERROR_NO_SUCH_TABLE(911, "There is no table with this number."),
    ERROR_PLAYER_ALREADY_ON_TABLE(912, "The player is already sitting at this table."),
    ERROR_TABLE_FULL(913, "The table has reached players limit.");

    private final int code;
    private final String errorMessage;

    WordsEvent(int code) {
        this.code = code;
        this.errorMessage = null;
    }

    WordsEvent(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    private static final Map<Integer, WordsEvent> codeLookupMap;

    static {
        codeLookupMap = new HashMap<>();
        for (WordsEvent event : WordsEvent.values()) {
            codeLookupMap.put(event.getCode(), event);
        }
    }

    public static WordsEvent fromEventCode(int code) {
        return codeLookupMap.get(code);
    }

    public boolean isError() {
        return errorMessage != null;
    }
}
