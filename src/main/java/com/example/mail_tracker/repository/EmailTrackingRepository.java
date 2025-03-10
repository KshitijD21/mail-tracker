package com.example.mail_tracker.repository;

import com.example.mail_tracker.entities.EmailTrackingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailTrackingRepository extends MongoRepository<EmailTrackingEntity, String> {

    EmailTrackingEntity findByEmailId(String email);
}
