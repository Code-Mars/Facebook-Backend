package com.facebook.data.utility;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.facebook.data.exception.DataException;


@RestControllerAdvice
public class ExceptionControllerAdvice {
	@Autowired
    private Environment environment;
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorInfo> handleIOException(IOException exception) {
        ErrorInfo error = new ErrorInfo(environment.getProperty("SERVICE.IO_EXCEPTION"));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<ErrorInfo> handleSOSException(DataException exception) {
        ErrorInfo error = new ErrorInfo(environment.getProperty(exception.getMessage()));
        return new ResponseEntity<>(error, exception.getStatus());
    }
}
