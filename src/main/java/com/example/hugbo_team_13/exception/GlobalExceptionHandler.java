package com.example.hugbo_team_13.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Global exception handler for the application.
 * Provides centralized handling of exceptions across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public String handleDuplicateResource(ResourceAlreadyExistsException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}



/*
 * Handles client-side validation errors and bad requests.
 * Maps IllegalArgumentException to HTTP 400 Bad Request responses.
 *
 * @param ex The IllegalArgumentException that was thrown
 * @return ResponseEntity containing error details and BAD_REQUEST status

@ExceptionHandler(IllegalArgumentException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
    logger.error("Bad request: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
            HttpStatus.BAD_REQUEST
    );
}
*/

/*
 * Handles unexpected server-side errors.
 * Maps uncaught RuntimeExceptions to HTTP 500 Internal Server Error responses.
 *
 * @param ex The RuntimeException that was thrown
 * @return ResponseEntity containing error details and INTERNAL_SERVER_ERROR status

@ExceptionHandler(RuntimeException.class)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    logger.error("Server error: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
    );
}
*/
