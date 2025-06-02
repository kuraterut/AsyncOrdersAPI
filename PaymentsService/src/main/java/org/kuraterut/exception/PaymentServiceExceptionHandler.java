package org.kuraterut.exception;

import org.kuraterut.dto.ErrorResponse;
import org.kuraterut.exception.exceptions.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PaymentServiceExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> accountNotFound(AccountNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

