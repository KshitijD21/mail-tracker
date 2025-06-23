package com.example.mail_tracker.entities;

import lombok.Data;

@Data
public class ComposeBoxEntity {
   private TrackingIDEntity trackingObject;
   private String[] to;
   private String subject;
}
