package com.example.mail_tracker.common;

import com.example.mail_tracker.serviceimpl.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ImageGenerationUtils {
    private final S3Service s3Service;

    @Autowired
    public ImageGenerationUtils(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String generateAndUploadImage() throws IOException {

        System.out.println("Code is inside generateAndUploadImage function");

        // Create a transparent image (1x1 pixel)
        int width = 1;
        int height = 1;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Color transparent = new Color(0, 0, 0, 0); // Fully transparent
        image.setRGB(0, 0, transparent.getRGB());

        // Convert image to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageData = baos.toByteArray();

        System.out.println("imageData " + imageData);

        // Upload image to S3 and get URL
        return s3Service.uploadImage(imageData);
    }
}
