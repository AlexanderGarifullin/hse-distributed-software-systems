package hse.dss.diss.advice;

import hse.dss.diss.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalExceptionHandler handles exceptions thrown throughout the application.
 * Specifically, it catches the {@link EntityNotFoundException} and returns a proper HTTP response.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles {@link EntityNotFoundException} by logging the error and returning a NOT_FOUND response.
     *
     * @param e the exception to handle
     * @return a ResponseEntity with HTTP status NOT_FOUND and the exception message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
