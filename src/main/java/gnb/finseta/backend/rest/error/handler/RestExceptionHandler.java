package gnb.finseta.backend.rest.error.handler;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;


@ControllerAdvice
public class RestExceptionHandler {


    Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(exception = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse handleUnexpectedException(Exception e) {

        logger.error(e.getMessage());
        return ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
                .build();
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException argumentNotValidException) {
        logger.info("Argument Not valid \n%s".formatted(argumentNotValidException.getMessage()));

        return ErrorResponse.builder(
                        argumentNotValidException,
                        HttpStatus.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
    }

    @ExceptionHandler(exception = NotImplementedException.class)
    public ErrorResponse handleNotImplementedException(NotImplementedException ex) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_IMPLEMENTED, "Not Implements")
                .build();
    }
}
