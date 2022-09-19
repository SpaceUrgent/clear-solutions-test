package com.test.userapp.advice;

import com.test.userapp.exception.DataProcessingException;
import com.test.userapp.exception.InvalidDataRangeInputs;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DataProcessingException.class, InvalidDataRangeInputs.class})
    protected ResponseEntity<Object> handleException(Exception exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("description", HttpStatus.BAD_REQUEST.toString());
        body.put("details", exception.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("description", status.toString());
        List<String> details = ex.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(this::getErrorMessage)
                        .collect(Collectors.toList());
        body.put("details", details);
        return new ResponseEntity<>(body, headers, status);
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            String field = ((FieldError) error).getField();
            return (field.contains("data.") ?
                    field.replace("data.", "")
                    : field)
                    + " " + error.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }


}
