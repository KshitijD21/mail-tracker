package com.example.mail_tracker.repository;


import com.example.mail_tracker.entities.TrackingLinkEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface TrackingLinkRepository extends MongoRepository<TrackingLinkEntity, String> {

    Optional<TrackingLinkEntity> findByCode(String code);
    List<TrackingLinkEntity> findAllByUserId(String userId);
    Integer countByUserId(String userId);

}
