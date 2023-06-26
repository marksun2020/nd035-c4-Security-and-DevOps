package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;


// https://spring.io/guides/gs/testing-web/
// https://howtodoinjava.com/spring-boot2/testing/testresttemplate-post-example/
// https://www.appsdeveloperblog.com/testresttemplate-http-post-example/

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerComplexTests {

    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;



    @Test
    public void greetingShouldReturnDefaultMessage()  {
        String url = "http://localhost:" + port + "/api/user/unknown_user";
        String result = this.restTemplate.getForObject(url, String.class);
        Assertions.assertTrue(result.contains("Forbidden"));
    }

    @Test
    void addNewUser() throws JsonProcessingException {
        String url = "http://localhost:" + port + "/api/user/create";
        JSONObject user = new JSONObject();
        user.put("username","Alexander");
        user.put("password", "abcde12345");
        user.put("confirmPassword", "abcde12345");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity(user.toString(), headers);

        ResponseEntity<String> response =restTemplate.postForEntity(url, request, String.class);
        JsonNode responseBody = new ObjectMapper().readTree(response.getBody());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, responseBody.get("id").asInt());
        Assertions.assertEquals("Alexander", responseBody.get("username").asText());

    }
}
