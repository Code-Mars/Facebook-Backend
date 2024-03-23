package com.facebook.data.exception;

import org.springframework.http.HttpStatus;

public class DataException extends RuntimeException{
    private static final long serialVersionUID = 7355608L;
    private final String message;
    private final HttpStatus status;

    public DataException() {
        message = "GENERAL_MESSAGE";
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public DataException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
    public HttpStatus getStatus() {
        return status;
    }
}
