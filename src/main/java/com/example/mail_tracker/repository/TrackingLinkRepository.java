package com.example.mail_tracker.repository;


import com.example.mail_tracker.entities.TrackingLinkEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface TrackingLinkRepository extends MongoRepository<TrackingLinkEntity, String> {

    Optional<TrackingLinkEntity> findByCode(String code);
}
