package com.example.demo_testing.Exception;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo_testing.Model.ApiResponse;
import com.example.demo_testing.Util.Response;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ DataIntegrityViolationException.class, ConflictException.class })
    public ResponseEntity<ApiResponse> handleConflict(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.createResponse(HttpStatus.CONFLICT, errors));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(NotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.createResponse(HttpStatus.NOT_FOUND, errors));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class, BadRequestException.class })
    public ResponseEntity<ApiResponse> handleBadRequest(Exception ex) {
        List<String> errors;
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validationEx = (MethodArgumentNotValidException) ex;
            errors = validationEx.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        } else {
            errors = Collections.singletonList(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.createResponse(HttpStatus.BAD_REQUEST, errors));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorized(UnauthorizedException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.createResponse(HttpStatus.UNAUTHORIZED, errors));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ BadCredentialsException.class, ForbiddenException.class })
    public ResponseEntity<ApiResponse> handleForbidden(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.createResponse(HttpStatus.FORBIDDEN, errors));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ RuntimeException.class, Exception.class })
    public ResponseEntity<ApiResponse> handleError(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, errors));
    }
}
