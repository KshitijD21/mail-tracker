package com.example.mail_tracker.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "google_tokens")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleTokenEntity {

    @Id
    private String id;

    private String userId;

    private String accessToken;

    private String refreshToken;

    private Instant accessTokenExpiresAt;

    private Instant createdAt;

    private Instant updatedAt;

    private String email;

}