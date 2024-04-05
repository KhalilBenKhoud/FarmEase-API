package com.pi.farmease.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
@AllArgsConstructor
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<String> test() {
            return ResponseEntity.ok().body("from test") ;

    }

}
