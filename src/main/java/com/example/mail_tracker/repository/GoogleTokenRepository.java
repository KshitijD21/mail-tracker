package com.example.mail_tracker.repository;

import com.example.mail_tracker.entities.GoogleTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleTokenRepository extends MongoRepository<GoogleTokenEntity, String> {

    GoogleTokenEntity findByEmail(String email);

}
