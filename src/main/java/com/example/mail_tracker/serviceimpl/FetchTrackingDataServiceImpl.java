package com.example.mail_tracker.serviceimpl;

import com.example.mail_tracker.DTO.AllTrackingLinkDTO;
import com.example.mail_tracker.DTO.DashboardMetricsDto;
import com.example.mail_tracker.entities.DateRangeRequest;
import com.example.mail_tracker.entities.TrackingDetailEntity;
import com.example.mail_tracker.entities.TrackingLinkEntity;
import com.example.mail_tracker.repository.TrackingDetailRepository;
import com.example.mail_tracker.repository.TrackingLinkRepository;
import com.example.mail_tracker.service.FetchTrackingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FetchTrackingDataServiceImpl implements FetchTrackingDataService {

    @Autowired
    private TrackingLinkRepository trackingLinkRepository;

    @Autowired
    private TrackingDetailRepository trackingDetailRepository;

    @Override
    public List<AllTrackingLinkDTO> getAllEmailData(String userId) throws Exception {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        return  trackingLinkRepository.findAllByUserId(userId).stream()
                .map (entity -> {
                    AllTrackingLinkDTO allTrackingLinkDTO = new AllTrackingLinkDTO();
                    allTrackingLinkDTO.setId(entity.getId());
                    allTrackingLinkDTO.setCode(entity.getCode());
                    allTrackingLinkDTO.setCreatedAt(formatToArizonaTime(entity.getCreatedAt()));
                    allTrackingLinkDTO.setSubject(entity.getSubject());
                    allTrackingLinkDTO.setRecipientEmail(entity.getRecipientEmail());
                    allTrackingLinkDTO.setOpened(entity.isOpened());
                    allTrackingLinkDTO.setUserId(entity.getUserId());
                    allTrackingLinkDTO.setTotalOpens(entity.getTotalOpens());

                    return allTrackingLinkDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public DashboardMetricsDto fetchDashboardMetrics(String userId) throws Exception {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        // log the user ID for debugging purposes
        System.out.println("Fetching dashboard metrics for user ID: " + userId);

        List<TrackingLinkEntity> trackingLinks = trackingLinkRepository.findAllByUserId(userId);
        if (trackingLinks == null || trackingLinks.isEmpty()) {
            return new DashboardMetricsDto(0, 0, 0, 0);
        }
        int totalEmails = trackingLinks.size();
        int emailOpenCount = (int) trackingLinks.stream()
                .filter(TrackingLinkEntity::isOpened)
                .count();
        // Count unique recipients (assumes getRecipient() returns email)
        long totalUniqueRecipients = trackingLinks.stream()
                .map(TrackingLinkEntity::getRecipientEmail)
                .distinct()
                .count();

        int openRate = (int) ((double) emailOpenCount / totalEmails * 100);

        return new DashboardMetricsDto(totalEmails, emailOpenCount, (int) totalUniqueRecipients, openRate);
    }

    @Override
    public List<Map<String, Object>> getOpenTimeChart(String trackingId, DateRangeRequest dateRangeRequest) throws Exception {
        List<TrackingDetailEntity> details = trackingDetailRepository.findByTrackingLinksWithInDateRange(
                trackingId, dateRangeRequest.getStartDate(), dateRangeRequest.getEndDate()
        );

        Map<LocalDate, Map<String, Integer>> grouped = new TreeMap<>();
        ZoneId arizonaZone = ZoneId.of("America/Phoenix");

        // Step 1: Pre-fill all dates in range with zero counts
        LocalDate start = dateRangeRequest.getStartDate().toInstant().atZone(arizonaZone).toLocalDate();
        LocalDate end = dateRangeRequest.getEndDate().toInstant().atZone(arizonaZone).toLocalDate();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            grouped.put(date, new HashMap<>(Map.of("morning", 0, "afternoon", 0, "evening", 0)));
        }

        // Step 2: Update actual tracking counts
        for (TrackingDetailEntity detail : details) {
            Date createdAtDate = detail.getCreatedAt();
            Instant instant = createdAtDate.toInstant();
            ZonedDateTime arizonaDateTime = instant.atZone(arizonaZone);

            LocalDate date = arizonaDateTime.toLocalDate();
            int hour = arizonaDateTime.getHour();

            String bucket = (hour >= 5 && hour < 12) ? "morning"
                    : (hour >= 12 && hour < 17) ? "afternoon"
                    : "evening";

            grouped.get(date).merge(bucket, 1, Integer::sum);
        }

        // Step 3: Convert to List<Map<String, Object>> for frontend
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<LocalDate, Map<String, Integer>> entry : grouped.entrySet()) {
            Map<String, Object> row = new HashMap<>();
            row.put("name", entry.getKey().toString()); // format as needed
            row.putAll(entry.getValue());
            result.add(row);
        }

        return result;
    }



    private String formatToArizonaTime(Date date) {
        ZoneId zoneId = ZoneId.of("America/Phoenix");
        ZonedDateTime zonedDateTime = date.toInstant().atZone(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"); // or any format you like

        return zonedDateTime.format(formatter);
    }
}
