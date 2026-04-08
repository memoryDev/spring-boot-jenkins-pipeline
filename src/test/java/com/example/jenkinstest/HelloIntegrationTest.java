package com.example.jenkinstest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void helloReturnsMessage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/hello", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Hello, Jenkins CI/CD!");
    }
}
