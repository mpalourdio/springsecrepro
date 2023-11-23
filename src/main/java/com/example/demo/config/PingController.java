package com.example.demo.config;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = {"/api", "/ws"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = "*/*")
public class PingController {

    @GetMapping("/ping")
    public ResponseEntity<List<String>> ping() {
        return ResponseEntity.ok(List.of("pong"));
    }
}
