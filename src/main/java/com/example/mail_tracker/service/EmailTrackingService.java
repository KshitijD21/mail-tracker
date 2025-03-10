package com.example.mail_tracker.service;

import com.example.mail_tracker.entities.EmailTrackingEntity;

public interface EmailTrackingService  {

    EmailTrackingEntity getTrackingData(String email);
}
