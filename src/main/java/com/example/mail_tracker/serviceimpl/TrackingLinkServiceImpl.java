package com.example.mail_tracker.serviceimpl;


import com.example.mail_tracker.entities.*;
import com.example.mail_tracker.entities.enums.TrackingType;
import com.example.mail_tracker.repository.DetailSummaryRepository;
import com.example.mail_tracker.repository.TrackingDetailRepository;
import com.example.mail_tracker.repository.TrackingLinkRepository;
import com.example.mail_tracker.repository.UserRepo;
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

@Service
public class TrackingLinkServiceImpl implements TrackingLinkService {

    @Autowired
    private TrackingLinkRepository trackingLinkRepository;

    @Autowired
    private TrackingDetailRepository trackingDetailRepository;

    @Autowired
    private DetailSummaryRepository detailSummaryRepository;

    private static final Logger logger = LoggerFactory.getLogger(TrackingLinkServiceImpl.class);
    @Autowired
    private UserRepo userRepo;

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

//    @Override
//    public ResponseEntity<Void> updateTrackingLinkData(String uniqueCode, String clientIp, String userAgent) {
//        try {
//            TrackingLinkEntity trackingLink = trackingLinkRepository.findByCode(uniqueCode).orElse(null);
//            if (trackingLink == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            Users sender = userRepo.findById(trackingLink.getUserId()).orElse(null);
//            if (sender == null) {
//                return ResponseEntity.notFound().build(); // or handle as needed
//            }
//
//            if (trackingLink.getRecipientEmail().equalsIgnoreCase(sender.getEmail())) {
//                return ResponseEntity.ok().build(); // Don't track self-open
//            }
//
//            TrackingDetailEntity detailEntity = TrackingDetailEntity.builder()
//                    .trackingLinkId(uniqueCode)
//                    .ip(clientIp)
//                    .userAgent(userAgent)
//                    .createdAt(new Date())
//                    .updatedAt(new Date())
//                    .build();
//            trackingDetailRepository.save(detailEntity);
//
//            trackingLink.setTotalOpens(trackingLink.getTotalOpens() + 1);
//            trackingLink.setLastOpenedAt(new Date());
//            trackingLink.setOpened(true);
//            trackingLink.setLastUserAgent(userAgent);
//            trackingLink.setLastClientIp(clientIp);
//
//
//            if (trackingLink.getFirstOpenedAt() == null) {
//                trackingLink.setFirstOpenedAt(new Date());
//            }
//
//            trackingLink.setUpdatedAt(new Date());
//            trackingLinkRepository.save(trackingLink);
//
//            return ResponseEntity.ok().build();
//
//
////
////            TrackingDetailEntity trackingDetailEntity = TrackingDetailEntity.builder()
////                    .trackingLinkId(uniqueCode)
////                    .ip(clientIp)
////                    .createdAt(new Date())
////                    .updatedAt(new Date())
////                    .userAgent(userAgent)
////                    .build();
////
////            boolean isNewUser = trackingDetailRepository.countByIpAndTrackingLinkId(clientIp, uniqueCode) == 0;
////
////            trackingDetailRepository.save(trackingDetailEntity);
////
////            long count = trackingDetailRepository.countTrackingDetails();
////
//////            System.out.println(trackingDetailRepository.findByIp(clientIp));
////
////            DetailSummaryEntity existingDetailSummary = detailSummaryRepository.findByTrackingLinkId(uniqueCode);
////
////            if (existingDetailSummary != null) {
////
////                existingDetailSummary.setTotalOpens(existingDetailSummary.getTotalOpens() + 1);
////                if (isNewUser) {
////                    existingDetailSummary.setUniqueUsers(existingDetailSummary.getUniqueUsers() + 1);
////                }
////                existingDetailSummary.setUpdatedAt(new Date());
////                detailSummaryRepository.save(existingDetailSummary);
////            } else {
////                DetailSummaryEntity detailSummaryEntity = DetailSummaryEntity.builder()
////                        .trackingLinkId(uniqueCode)
////                        .uniqueUsers(1)
////                        .totalOpens(1)
////                        .updatedAt(new Date())
////                        .build();
////
////                detailSummaryRepository.save(detailSummaryEntity);
////            }
//
////            return ResponseEntity.ok().build();
//
//        } catch (Exception e) {
//            logger.error("Error saving tracking details", e);
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//

@Override
public ResponseEntity<Void> updateTrackingLinkData(String uniqueCode, String clientIp, String userAgent) {
    try {
        logger.info("📩 Incoming tracking hit for ID: {}", uniqueCode);

        Optional<TrackingLinkEntity> optionalLink = trackingLinkRepository.findByCode(uniqueCode);
        if (optionalLink.isEmpty()) {
            logger.warn("❌ No tracking link found for code: {}", uniqueCode);
            return ResponseEntity.notFound().build();
        }

        TrackingLinkEntity trackingLink = optionalLink.get();
        logger.info("✅ Found tracking link: recipientEmail={}, userId={}", trackingLink.getRecipientEmail(), trackingLink.getUserId());

        // Load sender info
        Optional<Users> optionalSender = userRepo.findById(trackingLink.getUserId());
        if (optionalSender.isEmpty()) {
            logger.warn("❌ No sender user found with ID: {}", trackingLink.getUserId());
            return ResponseEntity.notFound().build();
        }

        Users sender = optionalSender.get();
        logger.info("🔐 Sender email: {}", sender.getEmail());

        // Safe null check
        String recipientEmail = trackingLink.getRecipientEmail();
        String senderEmail = sender.getEmail();

        if (recipientEmail != null && recipientEmail.equalsIgnoreCase(senderEmail)) {
            logger.info("🛑 Ignoring self-open by sender: {}", senderEmail);
            return ResponseEntity.ok().build(); // Don't track self-open
        }

        // (Optional) Save log of raw event
        TrackingDetailEntity detailEntity = TrackingDetailEntity.builder()
                .trackingLinkId(uniqueCode)
                .ip(clientIp)
                .userAgent(userAgent)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        trackingDetailRepository.save(detailEntity);
        logger.info("📝 Tracking detail saved for {}", uniqueCode);

        // Update analytics
        trackingLink.setTotalOpens(trackingLink.getTotalOpens() + 1);
        trackingLink.setLastOpenedAt(new Date());
        trackingLink.setOpened(true);
        trackingLink.setLastUserAgent(userAgent);
        trackingLink.setLastClientIp(clientIp);

        if (trackingLink.getFirstOpenedAt() == null) {
            trackingLink.setFirstOpenedAt(new Date());
        }

        trackingLink.setUpdatedAt(new Date());
        trackingLinkRepository.save(trackingLink);
        logger.info("✅ Tracking link updated for {}", uniqueCode);

        return ResponseEntity.ok().build();

    } catch (Exception e) {
        logger.error("❌ Error saving tracking details for ID: " + uniqueCode, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

    @Override
    public ResponseEntity<TrackingResponse> uploadTrackingId(ComposeBoxEntity composeBoxEntity, String userId) {

        try{
            System.out.println("composeBoxEntity " + composeBoxEntity);
            TrackingLinkEntity trackingLinkEntity = TrackingLinkEntity.builder()
                    .type(TrackingType.EMAIL)
                    .code(composeBoxEntity.getTrackingObject().getTrackingId())
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .recipientEmail(composeBoxEntity.getTo()[0])
                    .subject(composeBoxEntity.getSubject())
                    .userId(userId)

                    // summary defaults
                    .totalOpens(0)
                    .firstOpenedAt(null)
                    .lastOpenedAt(null)
                    .isOpened(false)
                    .lastUserAgent(null)
                    .lastClientIp(null)
                    .isFollowUp(false)
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