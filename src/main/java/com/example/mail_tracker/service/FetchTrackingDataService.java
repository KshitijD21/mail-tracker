package com.example.mail_tracker.service;

import com.example.mail_tracker.DTO.AllTrackingLinkDTO;
import com.example.mail_tracker.DTO.DashboardMetricsDto;
import com.example.mail_tracker.entities.DateRangeRequest;
import com.example.mail_tracker.entities.TrackingLinkEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface FetchTrackingDataService {

    List<AllTrackingLinkDTO> getAllEmailData(String userId) throws Exception;
    DashboardMetricsDto fetchDashboardMetrics(String userId) throws Exception;
    List<Map<String, Object>> getOpenTimeChart(String trackingId, DateRangeRequest dateRangeRequest) throws Exception;
}
