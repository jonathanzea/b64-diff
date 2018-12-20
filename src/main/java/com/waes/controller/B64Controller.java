package com.waes.controller;

import com.waes.dto.DiffResponse;
import com.waes.service.B64Service;
import org.omg.CORBA.portable.ValueOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
public class B64Controller {

    private final B64Service b64Service;

    @Autowired
    public B64Controller(B64Service b64Service) {
        this.b64Service = b64Service;
    }

    @PostMapping(path = "/v1/diff/{id}/left", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> registerLeftNodeData(@RequestBody final String jsonBase64Binary, @PathVariable(value = "id") Long id){
        b64Service.registerLeftNodeData(jsonBase64Binary, id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "/v1/diff/{id}/right", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> registerRightNodeData(@RequestBody final String jsonBase64Binary, @PathVariable(value = "id") Long id){
        b64Service.registerRightNodeData(jsonBase64Binary, id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/v1/diff/{id}")
    public ResponseEntity<DiffResponse> differentiateData(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(b64Service.compareNodeDataById(id));
    }


    @DeleteMapping(path = "/v1/diff/clear")
    public ResponseEntity<Void> clearNodesMap() {
        b64Service.clearNodesMap();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
