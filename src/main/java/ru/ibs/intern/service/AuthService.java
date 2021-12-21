package ru.ibs.intern.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.ibs.intern.entity.dictionaries.Currency;

import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String hhApiAuth = "https://hh.ru/oauth/token";

    @Value("${hh.username}")
    private String hhUserName;

    @Value("${hh.pass}")
    private String hhUserPass;


    public String getAuthToken() {

        String authUrl = "http://localhost:8090/fakeApiHH/oauth/token";

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=client_credentials" +
                             "&client_id=" + hhUserName + "&client_secret=" + hhUserPass;

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        /*ResponseEntity<JsonNode> token = restTemplate.exchange(hhApiAuth, HttpMethod.POST,
                requestEntity, JsonNode.class);*/

        ResponseEntity<JsonNode> token = restTemplate.exchange(authUrl, HttpMethod.POST,
                requestEntity, JsonNode.class);

        //  {"access_token": "{access_token}", "token_type": "bearer" }

        return Objects.requireNonNull(token.getBody()).get("access_token").asText();

    }
}
