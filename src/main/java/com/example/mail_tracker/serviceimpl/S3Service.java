package com.example.mail_tracker.serviceimpl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(S3Client s3Client, @Value("${aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String uploadImage(byte[] fileData) throws IOException {
        String fileName = "image_" + UUID.randomUUID() + ".png";

        System.out.println("code is inside uploadImage " + fileName);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("image/png")
                .build();

        System.out.println("code is inside uploadImage " + request);

        s3Client.putObject(request, RequestBody.fromBytes(fileData));

        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }


}
