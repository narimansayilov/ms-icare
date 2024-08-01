package com.icare.model.exception;

public class LimitExceededException extends RuntimeException{
    public LimitExceededException(String message) {
        super(message);
    }
}
