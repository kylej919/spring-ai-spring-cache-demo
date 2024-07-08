package com.kylej.ai.translator.aspect;

import com.kylej.ai.translator.entity.ErrorResponse;
import com.kylej.ai.translator.exception.InvalidLanguageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Intercepts exceptions thrown by the API controller and packages them into the desired response object
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(InvalidLanguageException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidLanguageException e) {
        ErrorResponse.builder().message(e.getMessage()).build();
        return ResponseEntity.badRequest().body(ErrorResponse.builder().message(e.getMessage()).build());
    }
}
