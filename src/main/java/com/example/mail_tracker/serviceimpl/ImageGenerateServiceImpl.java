package com.example.mail_tracker.serviceimpl;


import com.example.mail_tracker.common.ImageGenerationUtils;
import com.example.mail_tracker.service.ImageGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageGenerateServiceImpl implements ImageGenerateService {

//    @Autowired
//    ImageGenerateRepository imageGenerateRepository

    private final ImageGenerationUtils imageGenerationUtils;

    @Autowired
    public ImageGenerateServiceImpl(ImageGenerationUtils imageGenerationUtils) {
        this.imageGenerationUtils = imageGenerationUtils;
    }

    @Override
    public String generateImage() throws IOException {
        System.out.println("Code inside generateImage serviceImpl");
        return imageGenerationUtils.generateAndUploadImage();
    }

}
