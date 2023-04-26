package com.github.rodionovsasha.shoppinglist.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class RestExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Map<String, String> processValidationError(MethodArgumentNotValidException exception) {
        return processFieldErrors(exception.getBindingResult().getFieldErrors());
    }

    private  Map<String, String> processFieldErrors(List<FieldError> fieldErrors) {
        Map<String, String> errorMap = new HashMap<>();
        log.error("Validation errors:");
        fieldErrors.forEach(fieldError -> {
            log.error("Field '" + fieldError.getField() + "': " + fieldError.getDefaultMessage());
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return errorMap;
    }
}
