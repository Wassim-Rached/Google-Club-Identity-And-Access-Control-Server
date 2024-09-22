package com.example.usermanagement.dto;

import com.example.usermanagement.enums.StandardApiStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
public class StandardApiResponse<T>{
    private StandardApiStatus status;
    private String message;
    private T data;

    public StandardApiResponse(StandardApiStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public StandardApiResponse(StandardApiStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public StandardApiResponse(T data) {
        this.status = StandardApiStatus.SUCCESS;
        this.data = data;
    }

    public StandardApiResponse(String message) {
        this.status = StandardApiStatus.SUCCESS;
        this.message = message;
    }


}