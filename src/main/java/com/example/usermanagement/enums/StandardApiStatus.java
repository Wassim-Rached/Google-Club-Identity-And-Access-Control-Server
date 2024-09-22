package com.example.usermanagement.enums;

public enum StandardApiStatus {
    SUCCESS("success"),
    FAILURE("failure");

    private final String value;

    StandardApiStatus(String value) {
        this.value = value;
    }
}