package com.example.mail_tracker.repository;

import com.example.mail_tracker.entities.DetailSummaryEntity;
import com.example.mail_tracker.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailSummaryRepository extends MongoRepository<DetailSummaryEntity, String> {

    @Query("{ 'trackingLinkId' : ?0 }")
    DetailSummaryEntity findByTrackingLinkId(String trackingLinkId);

}
