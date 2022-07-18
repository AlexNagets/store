package com.store.advice;

import com.store.exception.NoRecordFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoRecordFoundAdvice {
    @ResponseBody
    @ExceptionHandler(NoRecordFoundException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String noRecordFoundExceptionHandler (NoRecordFoundException exception) {
        return exception.getMessage();
    }
}
