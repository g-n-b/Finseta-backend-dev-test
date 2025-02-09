package gnb.finseta.backend.rest.error.handler;

import gnb.finseta.backend.exceptions.InvalidRequestFieldException;
import org.openapitools.model.BadRequest;
import org.openapitools.model.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;


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
	public ResponseEntity<BadRequest> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException argumentNotValidException) {
		logger.info("Argument Not valid \n%s".formatted(argumentNotValidException.getMessage()));

		List<Error> listOfErrs = argumentNotValidException.getAllErrors().stream()
				.map(error -> Error.builder()
						.message(error.toString()
								.substring(0, Math.min(100, error.toString().length()))
								.concat("..."))
						.build())
				.toList();
		return ResponseEntity.badRequest()
				.body(BadRequest.builder().errors(listOfErrs).build());
	}

	@ExceptionHandler(exception = InvalidRequestFieldException.class)
	public ResponseEntity<BadRequest> handleInvalidRequestFieldException(InvalidRequestFieldException requestFieldException) {
		logger.info("Request failed validation %s".formatted(requestFieldException.getMessage()));

		return ResponseEntity.badRequest()
				.body(BadRequest.builder()
						.errors(List.of(Error.builder().message(requestFieldException.getMessage()).build()))
						.build());
	}
}
