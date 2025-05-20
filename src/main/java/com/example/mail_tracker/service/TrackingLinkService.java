package com.example.mail_tracker.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TrackingLinkService {
    ResponseEntity<Map<String, Object>> getTrackingLink(String userId, String clientIp);

    ResponseEntity<Void> updateTrackingLinkData(String uniqueCode, String clientIp, String userAgent);

    ResponseEntity<Boolean> uploadTrackingId(String trackingId, String userId);
}
