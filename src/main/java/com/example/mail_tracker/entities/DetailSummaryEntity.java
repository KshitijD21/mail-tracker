package com.example.mail_tracker.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "detail_summary")
public class DetailSummaryEntity {

    @Id
    private String id;

    @Field("tracking_link_id")
    private String trackingLinkId;

    private long totalOpens;
    private long uniqueUsers;
    private Date updatedAt;

    // Getters
    public String getId() {
        return id;
    }

    public String getTrackingLinkId() {
        return trackingLinkId;
    }

    public long getTotalOpens() {
        return totalOpens;
    }

    public long getUniqueUsers() {
        return uniqueUsers;
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

    public void setTotalOpens(long totalOpens) {
        this.totalOpens = totalOpens;
    }

    public void setUniqueUsers(long uniqueUsers) {
        this.uniqueUsers = uniqueUsers;
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
        private long totalOpens;
        private long uniqueUsers;
        private Date updatedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder trackingLinkId(String trackingLinkId) {
            this.trackingLinkId = trackingLinkId;
            return this;
        }

        public Builder totalOpens(long totalOpens) {
            this.totalOpens = totalOpens;
            return this;
        }

        public Builder uniqueUsers(long uniqueUsers) {
            this.uniqueUsers = uniqueUsers;
            return this;
        }

        public Builder updatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DetailSummaryEntity build() {
            DetailSummaryEntity entity = new DetailSummaryEntity();
            entity.setId(this.id);
            entity.setTrackingLinkId(this.trackingLinkId);
            entity.setTotalOpens(this.totalOpens);
            entity.setUniqueUsers(this.uniqueUsers);
            entity.setUpdatedAt(this.updatedAt);
            return entity;
        }
    }
}