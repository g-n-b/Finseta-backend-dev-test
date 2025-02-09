package gnb.finseta.backend.rest.error.handler;

import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RestExceptionHandlerTest {

    RestExceptionHandler subject = new RestExceptionHandler();

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
    void handleUnexpectedException() {
        fail();
    }

    @Test
    void handleMethodArgumentNotValidException() {
        fail();
    }

    @Test
    void handleInvalidRequestFieldException() {
        fail();
    }
}