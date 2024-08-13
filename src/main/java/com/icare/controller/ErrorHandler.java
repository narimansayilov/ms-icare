package com.icare.controller;

import com.icare.dto.exception.ExceptionResponse;
import com.icare.dto.exception.FieldErrorResponse;
import com.icare.dto.exception.ValidationExceptionResponse;
import com.icare.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandler {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MMMM-dd HH:mm:ss");

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundException exception) {
        return buildExceptionResponse(exception.getMessage(), NOT_FOUND.value(), "NOT_FOUND");
    }

    @ExceptionHandler(ActiveException.class)
    @ResponseStatus(CONFLICT)
    public ExceptionResponse handleActiveException(ActiveException exception) {
        return buildExceptionResponse(exception.getMessage(), CONFLICT.value(), "ACTIVE_CONFLICT");
    }

    @ExceptionHandler(NotActiveException.class)
    @ResponseStatus(CONFLICT)
    public ExceptionResponse handleNotActiveException(NotActiveException exception) {
        return buildExceptionResponse(exception.getMessage(), CONFLICT.value(), "NOT_ACTIVE_CONFLICT");
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handleFileUploadException(FileUploadException exception){
        return buildExceptionResponse(exception.getMessage(), BAD_REQUEST.value(), "FILE_UPLOAD_ERROR");
    }

    @ExceptionHandler(LimitExceededException.class)
    @ResponseStatus(FORBIDDEN)
    public ExceptionResponse handleLimitExceededException(LimitExceededException exception){
        return buildExceptionResponse(exception.getMessage(), FORBIDDEN.value(), "LIMIT_EXCEEDED");
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    public ExceptionResponse handleAlreadyExistsException(AlreadyExistsException exception) {
        return buildExceptionResponse(exception.getMessage(), CONFLICT.value(), "ALREADY_EXISTS");
    }

    @ExceptionHandler(DeletionException.class)
    @ResponseStatus(CONFLICT)
    public ExceptionResponse handleDeletionException(DeletionException exception){
        return buildExceptionResponse(exception.getMessage(), CONFLICT.value(), "DELETION_ERROR");
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ExceptionResponse handleUnauthorizedAccessException(UnauthorizedAccessException exception) {
        return buildExceptionResponse(exception.getMessage(), UNAUTHORIZED.value(), "UNAUTHORIZED_ACCESS");
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handleHandlerMethodValidationException(HandlerMethodValidationException exception) {
        return buildExceptionResponse(exception.getMessage(), BAD_REQUEST.value(), "HANDLER_METHOD_VALIDATION_ERROR");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception exception) {
        return buildExceptionResponse(exception.getMessage(), INTERNAL_SERVER_ERROR.value(), "UNEXPECTED_EXCEPTION");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ValidationExceptionResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<FieldErrorResponse> fieldErrors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            FieldErrorResponse error = FieldErrorResponse.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
            fieldErrors.add(error);
        });

        return buildValidationExceptionResponse(BAD_REQUEST.value(), fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ValidationExceptionResponse handleConstraintViolationException(ConstraintViolationException exception) {
        List<FieldErrorResponse> fieldErrors = exception
                .getConstraintViolations()
                .stream()
                .map(violation ->
                        FieldErrorResponse.builder()
                                .field(violation.getPropertyPath().toString())
                                .message(violation.getMessage())
                                .build())
                .collect(Collectors.toList());

        return buildValidationExceptionResponse(BAD_REQUEST.value(), fieldErrors);
    }

    private ValidationExceptionResponse buildValidationExceptionResponse(Integer status, List<FieldErrorResponse> fieldErrors) {
        return ValidationExceptionResponse.builder()
                .timestamp(LocalDateTime.now().format(DATE_FORMATTER))
                .code("VALIDATION_FAILED")
                .status(status)
                .fieldErrors(fieldErrors)
                .build();
    }

    private ExceptionResponse buildExceptionResponse(String message, Integer status, String code) {
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now().format(DATE_FORMATTER))
                .message(message)
                .code(code)
                .status(status)
                .build();
    }
}