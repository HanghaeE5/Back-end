package com.example.backend.common.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class CommonController {

    @GetMapping("/")
    public String home(){
        return "index";
    }


    @GetMapping("/test")
    public String test(){
        log.error("test");
        return "index";
    }
}
