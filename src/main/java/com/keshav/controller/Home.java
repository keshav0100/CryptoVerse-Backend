package com.keshav.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    @GetMapping
    public String home() {
        return "Hello from CryptoVerse!";
    }

    @GetMapping("/api")
    public String secure() {
    return "Hello from CryptoVerse Secure!";
    }

}
