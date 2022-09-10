package pl.szinton.querky.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler(value = {InvalidFormatException.class, MismatchedInputException.class})
    public void handleMessageConversionException(MessageConversionException ex) {
        log.error("Failed to convert incoming message: " + ex.getMessage());
    }
}
