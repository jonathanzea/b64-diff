package com.waes.dto;

public final class ErrorValidation {

    private final String type;
    private final String title;
    private final String detail;

    public ErrorValidation(Exception ex) {
        this.type = ex.getClass().getSimpleName();
        this.title = ex.getMessage();
        this.detail = ex.toString();
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }
}