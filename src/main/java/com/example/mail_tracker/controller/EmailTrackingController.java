package com.example.mail_tracker.controller;

import com.example.mail_tracker.entities.EmailTrackingEntity;
import com.example.mail_tracker.service.EmailTrackingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmailTrackingController {

    @Autowired
    private EmailTrackingService emailTrackingService;

    @GetMapping("/{email}")
    public ResponseEntity<byte[]> saveEmailTracking(
            @PathVariable String email,
            @RequestHeader Map<String, String> headers,
            HttpServletRequest request) throws IOException {

        System.out.println("Email: " + email);
        System.out.println("Request Headers: " + headers);

        // Get client IP address
        String clientIp = request.getRemoteAddr();

        // Check for IP behind proxy (like Cloudflare, AWS, etc.)
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            clientIp = forwardedFor.split(",")[0].trim(); // Get first IP in the list
        }

        System.out.println("Client IP: " + clientIp);

        // Create a 1x1 transparent image
        int width = 1, height = 1;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Color transparent = new Color(0, 0, 0, 0); // Fully transparent
        image.setRGB(0, 0, transparent.getRGB());

        // Convert image to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageData = baos.toByteArray();

        // Set response headers
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "image/png");
        responseHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
        responseHeaders.add("Pragma", "no-cache");
        responseHeaders.add("Expires", "0");

        System.out.println("Generated Image Data: " + imageData.length + " bytes");

        return new ResponseEntity<>(imageData, responseHeaders, HttpStatus.OK);
    }

}
