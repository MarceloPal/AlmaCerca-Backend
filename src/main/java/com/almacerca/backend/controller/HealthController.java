package com.almacerca.backend.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Almacerca Backend OK";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}