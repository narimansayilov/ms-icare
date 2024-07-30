package com.icare.controller;

import com.icare.model.dto.response.ExceptionResponse;
import com.icare.model.exception.AlreadyExistsException;
import com.icare.model.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MMMM-dd HH:mm:ss");

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundException exception) {
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now().format(DATE_FORMATTER))
                .message(exception.getMessage())
                .code("NOT_FOUND")
                .status(NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    public ExceptionResponse handleAlreadyExistsException(AlreadyExistsException exception) {
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now().format(DATE_FORMATTER))
                .message(exception.getMessage())
                .code("ALREADY_EXISTS")
                .status(CONFLICT.value())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception exception) {
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now().format(DATE_FORMATTER))
                .message(exception.getMessage())
                .code("UNEXPECTED_EXCEPTION")
                .status(INTERNAL_SERVER_ERROR.value())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request){
        List<ExceptionResponse> errors = new ArrayList<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(e -> errors.add(ExceptionResponse.builder()
                                .timestamp(LocalDateTime.now().format(DATE_FORMATTER))
                                .message(exception.getMessage())
                                .code("INVALID_ARGUMENT")
                                .status(BAD_REQUEST.value())
                        .build()));
        return ResponseEntity.status(status).body(errors);
    }
}
