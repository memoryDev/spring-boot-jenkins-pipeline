package com.example.jenkinstest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Jenkins CI/CD!";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "hello2=수정했습니다. = 운영서버 배포했는지?";
    }
}
