package com.example.mail_tracker.controller;

import com.example.mail_tracker.service.ImageGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ImageGenerateController {

    private final ImageGenerateService imageGenerateService;

    @Autowired
    public ImageGenerateController(ImageGenerateService imageGenerateService) {
        this.imageGenerateService = imageGenerateService;
    }

    @PostMapping("/generate/image")
    public ResponseEntity<String> generateImage(@RequestBody String userId) throws IOException {

        System.out.println("Code inside generateImage Controller " + userId);
        String imageUrl = imageGenerateService.generateImage();
        System.out.println("imageUrl " + imageUrl);

            // Return URL
        return ResponseEntity.ok(imageUrl);

    }
}
