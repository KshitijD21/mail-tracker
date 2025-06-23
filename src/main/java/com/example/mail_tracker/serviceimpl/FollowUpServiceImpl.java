package com.example.mail_tracker.serviceimpl;

import com.example.mail_tracker.DTO.AllTrackingLinkDTO;
import com.example.mail_tracker.entities.TrackingLinkEntity;
import com.example.mail_tracker.repository.TrackingLinkRepository;
import com.example.mail_tracker.service.FollowUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowUpServiceImpl implements FollowUpService {
    private final TrackingLinkRepository trackingLinkRepository;

    @Autowired
    public FollowUpServiceImpl(TrackingLinkRepository trackingLinkRepository) {
        this.trackingLinkRepository = trackingLinkRepository;
    }

    @Override
    public void setFollowUp(String trackingId, boolean isFollowUp) {
        TrackingLinkEntity link = trackingLinkRepository.findByCode(trackingId)
                .orElseThrow(() -> new RuntimeException("Tracking Link not found with ID: " + trackingId));
        link.setFollowUp(isFollowUp);
        trackingLinkRepository.save(link);
    }

    @Override
    public List<AllTrackingLinkDTO> getAllFollowUps() {
        List<TrackingLinkEntity> followUps = trackingLinkRepository.findAllByIsFollowUpTrue();
        return followUps.stream().map(this::convertToDTO).toList();
    }

    private AllTrackingLinkDTO convertToDTO(TrackingLinkEntity entity) {
        return new AllTrackingLinkDTO(
                entity.getId(),
                entity.getUserId(),
                entity.getCode(),
                entity.getRecipientEmail(),
                entity.getSubject(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
                entity.isOpened(),
                entity.getTotalOpens()
        );
    }
}
