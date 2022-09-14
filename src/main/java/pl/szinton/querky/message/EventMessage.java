package pl.szinton.querky.message;

import pl.szinton.querky.enums.WordsEvent;

import java.util.Arrays;
import java.util.List;

public record EventMessage(int c, List<Object> d) {

    public static EventMessage fromWordsEvent(WordsEvent event, Object... dataObjects) {
        int code = event.getCode();
        if (event.isError()) {
            List<Object> data = List.of(event.getErrorMessage());
            return new EventMessage(code, data);
        }
        List<Object> data = Arrays.asList(dataObjects);
        return new EventMessage(code, data);
    }

    public boolean isError() {
        return WordsEvent.fromEventCode(c).isError();
    }
}
