package pl.szinton.querky.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WordsLetterMatch {
    LETTER_NOT_IN_WORD(0),
    LETTER_MISPLACED(1),
    LETTER_CORRECT(2);

    private final int code;
}
