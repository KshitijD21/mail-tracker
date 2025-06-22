package com.example.mail_tracker.controller;

import com.example.mail_tracker.DTO.AllTrackingLinkDTO;
import com.example.mail_tracker.DTO.DashboardMetricsDto;
import com.example.mail_tracker.entities.DateRangeRequest;
import com.example.mail_tracker.entities.TrackingLinkEntity;
import com.example.mail_tracker.entities.UserPrinciple;
import com.example.mail_tracker.service.FetchTrackingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class FetchTrackingDataController {


    @Autowired
    private FetchTrackingDataService fetchTrackingDataService;

    @GetMapping("/allEmailData")
    public  List<AllTrackingLinkDTO> getAllEmailData() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            String userId = userPrinciple.getUser().getId();
            return fetchTrackingDataService.getAllEmailData(userId);
        } else {
            throw new RuntimeException("User is not authenticated");
        }
    }

    @GetMapping("/fetchDashboardMetrics")
    public DashboardMetricsDto fetchDashboardMetrics() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            String userId = userPrinciple.getUser().getId();
            return fetchTrackingDataService.fetchDashboardMetrics(userId);
        } else {
            throw new RuntimeException("User is not authenticated");
        }
    }

    @PostMapping("/open-chart/{trackingId}")
    public ResponseEntity<List<Map<String, Object>>> getOpenChartData(
            @PathVariable String trackingId,
            @RequestBody DateRangeRequest dateRangeRequest
            ) throws Exception {
        return ResponseEntity.ok(fetchTrackingDataService.getOpenTimeChart(trackingId, dateRangeRequest));
    }

}
