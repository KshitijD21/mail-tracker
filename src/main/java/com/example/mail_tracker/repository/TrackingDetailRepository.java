package com.example.mail_tracker.repository;

import com.example.mail_tracker.entities.TrackingDetailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrackingDetailRepository extends MongoRepository<TrackingDetailEntity, String> {

    @Query(value = "{}", count = true)
    Long countTrackingDetails();

   @Query(value = "{'ip': ?0, 'trackingLinkId': ?1}", count = true)
    long countByIpAndTrackingLinkId(String ip, String trackingLinkId);

    @Query("{'trackingLinkId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }")
   List<TrackingDetailEntity> findByTrackingLinksWithInDateRange(String trackingLinkId, Date startDate, Date endDate);
}
