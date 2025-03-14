package com.example.mail_tracker.repository;

import com.example.mail_tracker.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepo extends MongoRepository<Users, String> {

    @Query("{ 'userName' : ?0 }")
    Users findByUserName(String userName);

}
