package com.github.userservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.userservice.exception.UserNotFoundException;

@ControllerAdvice
public class UserAdvice {
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<Object> userNotFoundHandler(UserNotFoundException userNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .body("{\"errorMessage\":\"" + userNotFoundException.getMessage() + "\"}");
    }
}
