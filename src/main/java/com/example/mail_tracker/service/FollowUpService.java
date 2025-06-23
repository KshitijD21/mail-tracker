package com.example.mail_tracker.service;

import com.example.mail_tracker.DTO.AllTrackingLinkDTO;
import com.example.mail_tracker.entities.TrackingLinkEntity;

import java.util.List;

public interface FollowUpService {
    void setFollowUp(String trackingId, boolean isFollowUp);
    List<AllTrackingLinkDTO> getAllFollowUps();
}
