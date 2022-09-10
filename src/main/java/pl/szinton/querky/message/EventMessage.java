package pl.szinton.querky.message;

import pl.szinton.querky.enums.WordsEvent;

import java.util.Arrays;
import java.util.List;

public record EventMessage(int c, List<Object> d) {

    public EventMessage fromWordsEvent(WordsEvent event, Object... dataObjects) {
        int code = event.getCode();
        List<Object> data = Arrays.asList(dataObjects);
        return new EventMessage(code, data);
    }
}
