package com.example.mail_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllTrackingLinkDTO {

    private String id;
    private String userId;
    private String code;
    private String recipientEmail;
    private String subject;
    private String createdAt;
    private boolean opened;
    private int totalOpens;
}
