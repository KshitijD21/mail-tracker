package com.example.mail_tracker.service;

import com.example.mail_tracker.entities.ComposeBoxEntity;
import com.example.mail_tracker.entities.TrackingResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TrackingLinkService {
    ResponseEntity<Map<String, Object>> getTrackingLink(String userId, String clientIp);

    ResponseEntity<Void> updateTrackingLinkData(String uniqueCode, String clientIp, String userAgent);

    ResponseEntity<TrackingResponse> uploadTrackingId(ComposeBoxEntity trackingId, String userId);

    byte[] generateImage(int width, int height);

}
