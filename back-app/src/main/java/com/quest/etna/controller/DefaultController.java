package com.quest.etna.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    @PostMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String testSuccess() {
        return "success";
    }
}
