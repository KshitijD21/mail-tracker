package com.example.mail_tracker.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TrackingLinkService {
    ResponseEntity<Map<String, Object>> getTrackingLink(String userId, String clientIp);

    ResponseEntity<Void> updateTrackingLinkData(String uniqueCode, String userId, String clientIp, String userAgent);
}
