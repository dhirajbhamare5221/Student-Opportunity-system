package com.platform.opportunity.dto;

public class RecommendationDTO {
    
    private OpportunityDTO opportunity;
    private int matchScore;

    public RecommendationDTO(OpportunityDTO opportunity, int matchScore) {
        this.opportunity = opportunity;
        this.matchScore = matchScore;
    }

    // Getters and Setters
    public OpportunityDTO getOpportunity() { return opportunity; }
    public void setOpportunity(OpportunityDTO opportunity) { this.opportunity = opportunity; }

    public int getMatchScore() { return matchScore; }
    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }
}
