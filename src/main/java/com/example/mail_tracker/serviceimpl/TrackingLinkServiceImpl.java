package com.example.mail_tracker.serviceimpl;


import com.example.mail_tracker.entities.*;
import com.example.mail_tracker.entities.enums.TrackingType;
import com.example.mail_tracker.repository.DetailSummaryRepository;
import com.example.mail_tracker.repository.TrackingDetailRepository;
import com.example.mail_tracker.repository.TrackingLinkRepository;
import com.example.mail_tracker.service.TrackingLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Service
public class TrackingLinkServiceImpl implements TrackingLinkService {

    @Autowired
    private TrackingLinkRepository trackingLinkRepository;

    @Autowired
    private TrackingDetailRepository trackingDetailRepository;

    @Autowired
    private DetailSummaryRepository detailSummaryRepository;

    private static final Logger logger = LoggerFactory.getLogger(TrackingLinkServiceImpl.class);

    @Override
    public ResponseEntity<Map<String, Object>> getTrackingLink(String userId, String clientIp) {

        String uniqueCode = UUID.randomUUID().toString().toUpperCase().replace("-", "");

        System.out.println("UUID " + uniqueCode);
        String trackingUrl = "https://yourdomain.com/track/" + uniqueCode;

        TrackingLinkEntity trackingLinkEntity = TrackingLinkEntity.builder()
                .type(TrackingType.EMAIL)
                .code(uniqueCode)
                .createdAt(new Date())
                .updatedAt(new Date())
                .userId(userId)
                .build();

        trackingLinkRepository.save(trackingLinkEntity);

        byte[] imageData = generateTrackingPixel();

        Map<String, Object> response = new HashMap<>();
        response.put("trackingUrl", trackingUrl);
        response.put("imageData", Base64.getEncoder().encodeToString(imageData));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> updateTrackingLinkData(String uniqueCode, String clientIp, String userAgent) {
        try {
            TrackingDetailEntity trackingDetailEntity = TrackingDetailEntity.builder()
                    .trackingLinkId(uniqueCode)
                    .ip(clientIp)
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .userAgent(userAgent)
                    .build();

            boolean isNewUser = trackingDetailRepository.countByIpAndTrackingLinkId(clientIp, uniqueCode) == 0;

            trackingDetailRepository.save(trackingDetailEntity);

            long count = trackingDetailRepository.countTrackingDetails();

//            System.out.println(trackingDetailRepository.findByIp(clientIp));

            DetailSummaryEntity existingDetailSummary = detailSummaryRepository.findByTrackingLinkId(uniqueCode);

            if (existingDetailSummary != null) {

                existingDetailSummary.setTotalOpens(existingDetailSummary.getTotalOpens() + 1);
                if (isNewUser) {
                    existingDetailSummary.setUniqueUsers(existingDetailSummary.getUniqueUsers() + 1);
                }
                existingDetailSummary.setUpdatedAt(new Date());
                detailSummaryRepository.save(existingDetailSummary);
            } else {
                DetailSummaryEntity detailSummaryEntity = DetailSummaryEntity.builder()
                        .trackingLinkId(uniqueCode)
                        .uniqueUsers(1)
                        .totalOpens(1)
                        .updatedAt(new Date())
                        .build();

                detailSummaryRepository.save(detailSummaryEntity);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error saving tracking details", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<TrackingResponse> uploadTrackingId(ComposeBoxEntity composeBoxEntity, String userId) {

        try{
            TrackingLinkEntity trackingLinkEntity = TrackingLinkEntity.builder()
                    .type(TrackingType.EMAIL)
                    .code(composeBoxEntity.getTrackingObject().getTrackingId())
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .to(composeBoxEntity.getTo())
                    .subject(composeBoxEntity.getSubject())
                    .userId(userId)
                    .build();
            trackingLinkRepository.save(trackingLinkEntity);
            return ResponseEntity.ok(new TrackingResponse(true,composeBoxEntity.getTrackingObject().getTrackingId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TrackingResponse(false, composeBoxEntity.getTrackingObject().getTrackingId()));
        }
    }

    private byte[] generateTrackingPixel() {
        // Create a 1x1 transparent image
        int width = 1, height = 1;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Color transparent = new Color(0, 0, 0, 0); // Fully transparent
        image.setRGB(0, 0, transparent.getRGB());

        // Convert the image to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate tracking pixel", e);
        }
        return baos.toByteArray();
    }

    @Override
    public  byte[] generateImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Random random = new Random();
        Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, randomColor.getRGB());
            }
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate image", e);
        }
    }


}