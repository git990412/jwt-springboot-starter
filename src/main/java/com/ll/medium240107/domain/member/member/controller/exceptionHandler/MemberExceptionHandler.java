package com.ll.medium240107.domain.member.member.controller.exceptionHandler;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ll.medium240107.domain.member.member.controller.ApiV1MemberController;

@ControllerAdvice(basePackageClasses = ApiV1MemberController.class)
public class MemberExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        HashMap<String, String> data = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            data.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(data);
    }
}