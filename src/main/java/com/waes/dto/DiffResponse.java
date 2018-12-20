package com.waes.dto;

public class DiffResponse {

    private final String message;

    public DiffResponse(String equal) {
        this.message = equal;
    }

    public String getMessage() {
        return message;
    }
}
