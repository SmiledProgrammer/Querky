package pl.szinton.querky.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum WordsEvent {
    TABLE_DATA(101),
    PLAYER_JOINED_TABLE(102),
    PLAYER_LEFT_TABLE(103),
    PLAYER_READY(105),
    ROUND_COUNTDOWN_START(211),
    ROUND_GUESSING_PHASE_START(212),
    ROUND_END(213),
    RANDOM_WORD_RESPONSE(220),
    PLAYER_GUESS(221),

    ERROR_NO_SUCH_TABLE(911, "There is no table with this number."),
    ERROR_PLAYER_ALREADY_ON_TABLE(912, "The player is already sitting at this table."),
    ERROR_TABLE_FULL(913, "The table has reached players limit."),
    ERROR_PLAYER_NOT_ON_TABLE(921, "The player is not sitting at this table."),
    ERROR_DISALLOWED_WORD(922, "The typed word is invalid."),
    ERROR_PLAYER_HAS_NO_GUESSES_LEFT(923, "The player has no word guesses left in this round."),
    ERROR_PLAYER_HAS_ALREADY_GUESSED(924, "The player has already guessed the word correctly in this round."),
    ERROR_NOT_GUESSING_PHASE(925, "Can only submit guesses during guessing phase."),
    ERROR_NO_WORD_WITH_THIS_INDEX(951, "There is no word in the dictionary with this index.");

    private final int code;
    private String errorMessage;

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
