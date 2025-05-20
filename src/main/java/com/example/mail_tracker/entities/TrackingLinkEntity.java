package com.example.mail_tracker.entities;

import com.example.mail_tracker.entities.enums.TrackingType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

//@Document(collection = "tracking_link")
//@Data
//@Builder
//public class TrackingLinkEntity {
//
//    @Id
//    private String id;
//
//    private TrackingType type;
//
//    @Field("userId")
//    private String userId;
//
//    private String code;
//    private Date createdAt;
//    private Date updatedAt;
//}

@Document(collection = "tracking_links")
@Data
public class TrackingLinkEntity {

    @Id
    private String id;

    private TrackingType type;

    @Field("userId")
    private String userId;

    private String code;
    private Date createdAt;
    private Date updatedAt;

    // Manual setters
    public void setId(String id) {
        this.id = id;
    }

    public void setType(TrackingType type) {
        this.type = type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Manual builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private TrackingType type;
        private String userId;
        private String code;
        private Date createdAt;
        private Date updatedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder type(TrackingType type) {
            this.type = type;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public TrackingLinkEntity build() {
            TrackingLinkEntity entity = new TrackingLinkEntity();
            entity.setId(this.id);
            entity.setType(this.type);
            entity.setUserId(this.userId);
            entity.setCode(this.code);
            entity.setCreatedAt(this.createdAt);
            entity.setUpdatedAt(this.updatedAt);
            return entity;
        }
    }
}
