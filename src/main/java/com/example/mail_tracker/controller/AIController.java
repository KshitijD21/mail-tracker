package com.example.mail_tracker.controller;

import com.example.mail_tracker.entities.EmailBody;
import com.example.mail_tracker.service.GeminiApiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AIController {

  @Autowired
  private GeminiApiService geminiApiService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping("/getResponse")
  public ResponseEntity<String> getResponse( @RequestBody EmailBody emailBody) throws IOException {

    System.out.println("Received email body: " + emailBody.getEmailBody());
    String prompt = buildPrompt(emailBody.getEmailBody());

    String geminiResponseText = geminiApiService.sendTextToGemini(prompt);
    String cleanedResponse = geminiResponseText.trim();

    if (cleanedResponse.startsWith("⁠  json")) {
      cleanedResponse = cleanedResponse.replace("  ⁠json", "").replace("```", "").trim();
    }

    return ResponseEntity.ok().body(cleanedResponse);
  }

  private String buildPrompt(String emailBody) {
    return String.format("""
      You are an assistant helping to write a professional email reply.
      Here is the original email:
      %s
      Please write a reply that:
        - Addresses the sender by name (if available).
        - Responds to all key points or questions.
        - Maintains a polite and professional tone.
        - Closes with an appropriate sign-off.
      Keep it under 15 to 20 lines.
      """, emailBody);
  }
}