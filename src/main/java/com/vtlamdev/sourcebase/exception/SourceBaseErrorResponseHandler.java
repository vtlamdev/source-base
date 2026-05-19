package com.vtlamdev.sourcebase.exception;

import com.vtlamdev.sourcebase.common.data.exception.SourceBaseErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SourceBaseErrorResponseHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<SourceBaseErrorResponse> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new SourceBaseErrorResponse(exception.getErrorCode(), exception.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, IllegalArgumentException.class})
    public ResponseEntity<SourceBaseErrorResponse> handleValidation(Exception exception, HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(new SourceBaseErrorResponse(SourceBaseErrorCode.VALIDATION, exception.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<SourceBaseErrorResponse> handleBadCredentials(BadCredentialsException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new SourceBaseErrorResponse(SourceBaseErrorCode.UNAUTHORIZED, exception.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(SourceBaseException.class)
    public ResponseEntity<SourceBaseErrorResponse> handleSourceBaseException(SourceBaseException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new SourceBaseErrorResponse(exception.getErrorCode(), exception.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SourceBaseErrorResponse> handleUnhandled(Exception exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SourceBaseErrorResponse(SourceBaseErrorCode.INTERNAL, exception.getMessage(), request.getRequestURI()));
    }

}
