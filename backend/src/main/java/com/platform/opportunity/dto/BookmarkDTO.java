package com.platform.opportunity.dto;

import java.time.LocalDateTime;

public class BookmarkDTO {
    private Long id;
    private OpportunityDTO opportunity;
    private LocalDateTime savedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OpportunityDTO getOpportunity() { return opportunity; }
    public void setOpportunity(OpportunityDTO opportunity) { this.opportunity = opportunity; }

    public LocalDateTime getSavedAt() { return savedAt; }
    public void setSavedAt(LocalDateTime savedAt) { this.savedAt = savedAt; }
}
