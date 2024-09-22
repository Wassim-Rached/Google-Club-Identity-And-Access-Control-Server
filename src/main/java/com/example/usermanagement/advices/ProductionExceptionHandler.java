package com.example.usermanagement.advices;

import com.example.usermanagement.dto.StandardApiResponse;
import com.example.usermanagement.enums.StandardApiStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Profile({"prod"})
@RequiredArgsConstructor
public class ProductionExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardApiResponse<Void>> handleException(Exception e) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, "Something Went Wrong!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}