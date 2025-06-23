package com.example.mail_tracker.controller;

import com.example.mail_tracker.DTO.AllTrackingLinkDTO;
import com.example.mail_tracker.service.FollowUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/followup")
public class FollowUpController {

    private final FollowUpService followUpService;

    @Autowired
    public FollowUpController(FollowUpService followUpService) {
        this.followUpService = followUpService;
    }


    @PostMapping("/set")
    public ResponseEntity<String> setFollowUp(
            @RequestParam String trackingId,
            @RequestParam boolean isFollowUp
    ) {
        followUpService.setFollowUp(trackingId, isFollowUp);
        return ResponseEntity.ok("Follow-up status updated successfully.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<AllTrackingLinkDTO>> getAllFollowUps() {
        List<AllTrackingLinkDTO> followUps = followUpService.getAllFollowUps();
        return ResponseEntity.ok(followUps);
    }


}
