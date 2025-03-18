package com.example.mail_tracker.entities;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "tracking_detail")
public class TrackingDetailEntity {

    @Id
    private String id;

    @Field("tracking_link_id")
    private String trackingLinkId;

    private String userAgent;
    private String ip;
    private Date createdAt;
    private Date updatedAt;

    // Getters
    public String getId() {
        return id;
    }

    public String getTrackingLinkId() {
        return trackingLinkId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIp() {
        return ip;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTrackingLinkId(String trackingLinkId) {
        this.trackingLinkId = trackingLinkId;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String trackingLinkId;
        private String userAgent;
        private String ip;
        private Date createdAt;
        private Date updatedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder trackingLinkId(String trackingLinkId) {
            this.trackingLinkId = trackingLinkId;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder ip(String ip) {
            this.ip = ip;
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

        public TrackingDetailEntity build() {
            TrackingDetailEntity entity = new TrackingDetailEntity();
            entity.setId(this.id);
            entity.setTrackingLinkId(this.trackingLinkId);
            entity.setUserAgent(this.userAgent);
            entity.setIp(this.ip);
            entity.setCreatedAt(this.createdAt);
            entity.setUpdatedAt(this.updatedAt);
            return entity;
        }
    }
}