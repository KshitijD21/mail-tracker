package com.example.mail_tracker.controller;


import com.example.mail_tracker.common.TrackingUtils;
import com.example.mail_tracker.entities.*;
import com.example.mail_tracker.service.TrackingLinkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TrackingLinkController {

    @Autowired
    private TrackingLinkService trackingLinkService;

    @GetMapping("/trackingLink")
    private ResponseEntity<Map<String, Object>> getTrackingLink(HttpServletRequest request) {

        System.out.println("code is inside getTrackingLink ");

        String clientIp = request.getRemoteAddr();
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            clientIp = forwardedFor.split(",")[0].trim();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            String userId = userPrinciple.getUser().getId();
            return trackingLinkService.getTrackingLink(userId, clientIp);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
    }

    @GetMapping("/track/{uniqueCode}")
    public ResponseEntity<byte[]> trackLink(@PathVariable String uniqueCode, HttpServletRequest request) {
        System.out.println("request is here " + uniqueCode);

        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            clientIp = forwardedFor.split(",")[0].trim();
        }

        trackingLinkService.updateTrackingLinkData(uniqueCode, clientIp, userAgent);

        byte[] imageBytes = trackingLinkService.generateImage(10, 10); // see helper class below

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageBytes);
    }


    @PostMapping("/tracking/ids")
    public ResponseEntity<TrackingResponse> uploadTrackingId(@RequestBody ComposeBoxEntity composeBoxEntity) {

        TrackingIDEntity tracking = composeBoxEntity.getTrackingObject();
        String expectedId = TrackingUtils.generateTrackingId(tracking.getU(), tracking.getK(), tracking.getT());
        boolean isValid = expectedId.equals(tracking.getTrackingId());

        System.out.println("Expected ID: " + expectedId);
        System.out.println("Received ID: " + tracking.getTrackingId());
        System.out.println("✅ Valid tracking ID? " + isValid);

        System.out.println("composeBoxEntity " + composeBoxEntity);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!isValid) {
            return ResponseEntity.ok(new TrackingResponse(false, tracking.getTrackingId()));
        }

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            String userId = userPrinciple.getUser().getId();
            return trackingLinkService.uploadTrackingId(composeBoxEntity, userId);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TrackingResponse(false, null));
    }

}
