package com.example.mail_tracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiApiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=";

    public String sendTextToGemini(String inputText) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> part = new HashMap<>();
        part.put("text", inputText);

        Map<String, Object> content = new HashMap<>();
        content.put("role", "user");
        content.put("parts", Collections.singletonList(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", Collections.singletonList(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        System.out.println("apiKey " + apiKey);

        ResponseEntity<Map> response = restTemplate.postForEntity(GEMINI_API_URL + apiKey, request, Map.class);

        System.out.println("response " + response);
        if (response.getStatusCode().is2xxSuccessful()) {
            Map responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("candidates")) {
                Map firstCandidate = (Map) ((List) responseBody.get("candidates")).get(0);
                Map contentMap = (Map) firstCandidate.get("content");
                List parts = (List) contentMap.get("parts");
                Map firstPart = (Map) parts.get(0);
                return (String) firstPart.get("text");
            }
        }

        return "Failed to get response from Gemini.";
    }
}