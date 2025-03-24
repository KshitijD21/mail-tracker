package com.example.mail_tracker.controller;


import com.example.mail_tracker.entities.UserPrinciple;
import com.example.mail_tracker.entities.Users;
import com.example.mail_tracker.service.TrackingLinkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/trackingLink" )
    private ResponseEntity<Map<String, Object>> getTrackingLink(HttpServletRequest request) {

        String clientIp = request.getRemoteAddr();
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            clientIp = forwardedFor.split(",")[0].trim();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            String userId = userPrinciple.getUser().getId();
            return trackingLinkService.getTrackingLink(userId,clientIp );
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
    }

//    @GetMapping("/track/{uniqueCode}")
//        public ResponseEntity<Void> trackLink(@PathVariable String uniqueCode, HttpServletRequest request) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            String clientIp = request.getRemoteAddr();
//            String userAgent = request.getHeader("User-Agent");
//            System.out.println("userAgent " + userAgent);
//            String forwardedFor = request.getHeader("X-Forwarded-For");
//            if (forwardedFor != null && !forwardedFor.isEmpty()) {
//                clientIp = forwardedFor.split(",")[0].trim();
//            }
//            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
//            String userId = userPrinciple.getUser().getId();
//            return trackingLinkService.updateTrackingLinkData(uniqueCode, userId, clientIp, userAgent);
//        }
//
//    }


}
