package com.example.mail_tracker.controller;

import com.example.mail_tracker.config.GoogleConfig;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStreamReader;
import java.util.*;

@RestController
public class GoogleAuthController {

    private static final String APPLICATION_NAME = "Gmail API Spring Boot";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final GoogleConfig googleConfig;

    @Value("${google.redirect.uri}")
    public String redirectUri;

    public GoogleAuthController(GoogleConfig googleConfig) {
        this.googleConfig = googleConfig;
    }

    GoogleAuthorizationCodeFlow getFlow(List<String> scopes) throws Exception {
        NetHttpTransport httpTransport = new NetHttpTransport();
        ClassPathResource resource = new ClassPathResource("credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(resource.getInputStream()));

        return new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, scopes)
                .setAccessType("offline")
                .build();
    }

    @GetMapping("/connect/google")
    public ResponseEntity<Map<String, String>> getGoogleOAuthUrl() throws Exception {
        System.out.println("code is inside getGoogleOAuthUrl");
        List<String> fullScopes = Arrays.asList(
                "https://www.googleapis.com/auth/gmail.readonly",
                "https://www.googleapis.com/auth/userinfo.email",
                "https://www.googleapis.com/auth/userinfo.profile"
        );
        GoogleAuthorizationCodeFlow flow =  getFlow(fullScopes);
        String url = flow.newAuthorizationUrl().setRedirectUri(redirectUri).set("prompt", "consent").build();
        System.out.println("url isssss " + url);

        Map<String, String> response = new HashMap<>();
        response.put("authUrl", url);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/oauth/callback")
//    public ResponseEntity<?> handleOAuthCallback(@RequestParam("code") String code) {
//        try {
//            TokenResponse tokenResponse = flow.newTokenRequest(code)
//                    .setRedirectUri(redirectUri)
//                    .execute();
//
//            Credential credential = flow.createAndStoreCredential(tokenResponse, "user");
//
//            // You now have access/refresh tokens for this Gmail user
//
//            // Optional: Save it in DB for future use
//
//            // Redirect user back to frontend
//            URI redirectUri = URI.create("http://localhost:3000/connected-successfully");
//            return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("OAuth failed");
//        }
//    }


//    @GetMapping("/oauth/callback")
//    public ResponseEntity<String> handleOAuthCallback(@RequestParam("code") String code) {
//        try {
//            System.out.println("code is this " + code);
//            GoogleAuthorizationCodeFlow flow = googleConfig.getFlow();
//            TokenResponse tokenResponse = flow.newTokenRequest(code)
//                    .setRedirectUri(redirectUri)
//                    .execute();
//
//            Credential credential = flow.createAndStoreCredential(tokenResponse, "user");
//
//            return ResponseEntity.ok("OAuth Success! Tokens stored.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OAuth Failed: " + e.getMessage());
//        }
//    }

}
