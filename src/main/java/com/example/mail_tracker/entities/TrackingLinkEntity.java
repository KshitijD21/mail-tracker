package com.example.mail_tracker.entities;

import com.example.mail_tracker.entities.enums.TrackingType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "tracking_links")
@Data
@Builder
public class TrackingLinkEntity {

    @Id
    private String id;

    private TrackingType type;

    @Field("userId")
    private String userId;

    private String code;
    private Date createdAt;
    private Date updatedAt;
    private String[] to;
    private String subject;
}
