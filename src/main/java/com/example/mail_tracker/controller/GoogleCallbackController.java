package com.example.mail_tracker.controller;

import com.example.mail_tracker.entities.GoogleTokenEntity;
import com.example.mail_tracker.entities.Users;
import com.example.mail_tracker.repository.GoogleTokenRepository;
import com.example.mail_tracker.repository.UserRepo;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
public class GoogleCallbackController {

    private final GoogleAuthController googleAuthController;

    public GoogleCallbackController(GoogleAuthController googleAuthController) {
        this.googleAuthController = googleAuthController;
    }

    @Autowired
    private UserRepo userRepo;

    Gson gson = new Gson();

    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/gmail.readonly",
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/userinfo.profile"
    );

    @Autowired
    private GoogleTokenRepository googleTokenRepository;

//    @GetMapping("/oauth/callback")
//    public ResponseEntity<Map<String, String>> handleGoogleCallback(@RequestParam("code") String code) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            GoogleAuthorizationCodeFlow flow = googleAuthController.getFlow();
//            GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
//                    .setRedirectUri(googleAuthController.redirectUri)
//                    .execute();
//
//            String accessToken = tokenResponse.getAccessToken();
//            String refreshToken = tokenResponse.getRefreshToken();
//
//            // TODO: Store tokens in the database for the user
//            response.put("message", "Google account connected successfully");
//            response.put("accessToken", accessToken);
//            response.put("refreshToken", refreshToken);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            response.put("error", "Failed to exchange code for tokens: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//    }

    @GetMapping("/oauth/callback")
    public ResponseEntity<Void> handleGoogleCallback(@RequestParam("code") String code) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = "";
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String userEmail = userDetails.getUsername(); // email stored in JWT
                Users user = userRepo.findByEmail(userEmail);
                userId = user.getId();
            }

            System.out.println("userid isss " + userId );

            GoogleAuthorizationCodeFlow flow = googleAuthController.getFlow(SCOPES);
            GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                    .setRedirectUri(googleAuthController.redirectUri)
                    .execute();

            System.out.println(" tokenResponse.getAccessToken() "+ tokenResponse.getAccessToken());
            HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(
                    request -> request.getHeaders().setAuthorization("Bearer " + tokenResponse.getAccessToken())
            );

            GenericUrl url = new GenericUrl("https://www.googleapis.com/oauth2/v2/userinfo");
            HttpRequest request = requestFactory.buildGetRequest(url);
            String jsonIdentity = request.execute().parseAsString();

            JsonObject data = new Gson().fromJson(jsonIdentity, JsonObject.class);
            String email = data.get("email").getAsString();

            System.out.println("email " + email);

            Instant expiresAt = Instant.now().plusSeconds(tokenResponse.getExpiresInSeconds());

            GoogleTokenEntity existingId = googleTokenRepository.findByEmail(email);
            String id = (existingId != null) ? existingId.getId() : null;

            GoogleTokenEntity tokenEntity = GoogleTokenEntity.builder()
                    .id(id)
                    .userId(userId)
                    .accessToken(tokenResponse.getAccessToken())
                    .refreshToken(tokenResponse.getRefreshToken())
                    .accessTokenExpiresAt(expiresAt)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .email(email)
                    .build();

            googleTokenRepository.save(tokenEntity);

            // Redirect back to frontend
            String redirectUrl = "http://localhost:3000/onboarding?status=true";
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .location(URI.create(redirectUrl))
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            String errorRedirect = "http://localhost:3000/oauth-error";
            String encodedError;
            try {
                encodedError = URLEncoder.encode(e.getMessage(), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                encodedError = "Unknown%20error";
            }

            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .location(URI.create(errorRedirect + "?error=" + encodedError))
                    .build();
        }
    }

}
