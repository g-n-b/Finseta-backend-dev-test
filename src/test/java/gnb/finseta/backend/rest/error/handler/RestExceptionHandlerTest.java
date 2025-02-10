package gnb.finseta.backend.rest.error.handler;

import gnb.finseta.backend.exceptions.InvalidRequestFieldException;
import gnb.finseta.backend.logging.IRequestLogger;
import org.junit.jupiter.api.Test;
import org.openapitools.model.BadRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class RestExceptionHandlerTest {

    RestExceptionHandler subject = new RestExceptionHandler(mock(IRequestLogger.class));

    @Test
    void thatGenericExceptionsAreCaughtAndReturnInternalErr() {
        var result  = subject.handleUnexpectedException(new Exception("err"));

        assertEquals("problemDetail.java.lang.Exception", result.getDetailMessageCode());
        assertEquals(500, result.getStatusCode().value());

        ProblemDetail resultBody = result.getBody();
        assertEquals("Internal Server Error", resultBody.getTitle());
        assertEquals("Internal Server Error", resultBody.getDetail());
    }

    @Test
    void handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        ObjectError err = mock(ObjectError.class);

        doReturn("Method Arg Not Valid")
                .when(ex)
                .getMessage();
        doReturn(List.of(err))
                .when(ex)
                .getAllErrors();
        doReturn("error specifics").when(err)
                .toString();

        var res = subject.handleMethodArgumentNotValidException(ex);

        assertEquals(HttpStatusCode.valueOf(400), res.getStatusCode());
        assertTrue(res.hasBody());
        BadRequest body = res.getBody();
        assertEquals(1, body.getErrors().size());
        assertEquals("error specifics...", body.getErrors().get(0).getMessage());
    }

    @Test
    void handleInvalidRequestFieldException() {
        InvalidRequestFieldException ex = mock(InvalidRequestFieldException.class);

        doReturn("Invalid Field Value")
                .when(ex)
                .getMessage();

        var res = subject.handleInvalidRequestFieldException(ex);

        assertEquals(HttpStatusCode.valueOf(400), res.getStatusCode());
        assertTrue(res.hasBody());
        var body = res.getBody();
        assertEquals(1, body.getErrors().size());
        assertEquals("Invalid Field Value", body.getErrors().get(0).getMessage());
    }
}