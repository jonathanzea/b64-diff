package com.waes.controller;

import com.waes.exception.*;
import com.waes.dto.ErrorValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice(assignableTypes = B64Controller.class)
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class B64ExceptionHandler {

    @ExceptionHandler(NodeDataAlreadyAddedException.class)
    public ResponseEntity handleNodeDataAlreadyAddedException(NodeDataAlreadyAddedException ex) {
        return new ResponseEntity(new ErrorValidation(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmptyDataForNodeException.class)
    public ResponseEntity handleEmptyDataForNodeException(EmptyDataForNodeException ex) {
        return new ResponseEntity(new ErrorValidation(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NodeDataEmptyForComparisonException.class)
    public ResponseEntity handleNodeDataEmptyForComparisonException(NodeDataEmptyForComparisonException ex) {
        return new ResponseEntity(new ErrorValidation(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(JsonMalformedException.class)
    public ResponseEntity handleJsonMalformedException(JsonMalformedException ex) {
        return new ResponseEntity(new ErrorValidation(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NodeNotExistsException.class)
    public ResponseEntity handleNodeNotExistsException(NodeNotExistsException ex) {
        return new ResponseEntity(new ErrorValidation(ex), HttpStatus.NOT_FOUND);
    }
}