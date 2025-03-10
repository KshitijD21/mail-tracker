package com.example.mail_tracker.serviceimpl;

import com.example.mail_tracker.entities.EmailTrackingEntity;
import com.example.mail_tracker.repository.EmailTrackingRepository;
import com.example.mail_tracker.service.EmailTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailTrackingServiceImpl implements EmailTrackingService {

    @Autowired
    private EmailTrackingRepository emailTrackingRepository;

    public EmailTrackingEntity getTrackingData(String email) {

        EmailTrackingEntity trackingData = emailTrackingRepository.findByEmailId(email);
        if (trackingData == null) {
            System.out.println("No data found for email: " + email);
            return null;
        }
        trackingData.setId("1245");
        System.out.println(trackingData.getId());
        return emailTrackingRepository.findByEmailId(email);
    }
}
