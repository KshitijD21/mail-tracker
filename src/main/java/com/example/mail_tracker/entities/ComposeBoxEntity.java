package com.example.mail_tracker.entities;

import lombok.Data;

@Data
public class ComposeBoxEntity {
   private TrackingIDEntity trackingObject;
   private String recipientEmail;
   private String subject;
}
