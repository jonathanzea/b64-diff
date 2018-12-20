package com.waes.exception;

public class JsonMalformedException extends RuntimeException {
    public JsonMalformedException(String message) {
        super(message);
    }
}
