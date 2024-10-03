package com.example.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DependencyStatus {
    private String status;
    private Integer code;
    private String message;

    public DependencyStatus(String status, Integer code) {
        this.status = status;
        this.code = code;
    }
}
