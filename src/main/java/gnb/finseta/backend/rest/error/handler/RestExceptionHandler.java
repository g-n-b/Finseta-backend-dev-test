package gnb.finseta.backend.rest.error.handler;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(exception = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse handleUnexpectedException(Exception e) {
        return ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")

                .build();
    }

    @ExceptionHandler(exception = NotImplementedException.class)
    public ErrorResponse handleNotImplementedException(NotImplementedException ex) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_IMPLEMENTED, "Not Implements")
                .build();
    }
}
