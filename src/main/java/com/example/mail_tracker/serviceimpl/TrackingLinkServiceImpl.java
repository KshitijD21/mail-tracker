package com.example.mail_tracker.serviceimpl;


import com.example.mail_tracker.entities.TrackingDetailEntity;
import com.example.mail_tracker.entities.TrackingLinkEntity;
import com.example.mail_tracker.entities.enums.TrackingType;
import com.example.mail_tracker.repository.TrackingLinkRepository;
import com.example.mail_tracker.service.TrackingLinkService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Void> updateTrackingLinkData(String uniqueCode, String userId, String clientIp, String userAgent) {

        TrackingDetailEntity trackingDetailEntity = TrackingDetailEntity.builder()
                .trackingLinkId(uniqueCode)
                .ip(clientIp)
                .createdAt(new Date())
                .updatedAt(new Date())
                .userAgent(userAgent)
                .build();





        return null;
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
}


