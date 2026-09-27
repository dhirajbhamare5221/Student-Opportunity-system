package com.platform.opportunity.service;

import com.platform.opportunity.dto.OpportunityDTO;
import com.platform.opportunity.dto.RecommendationDTO;
import com.platform.opportunity.model.Category;
import com.platform.opportunity.model.Opportunity;
import com.platform.opportunity.model.Skill;
import com.platform.opportunity.model.StudentProfile;
import com.platform.opportunity.repository.OpportunityRepository;
import com.platform.opportunity.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Transactional(readOnly = true)
    public List<RecommendationDTO> getRecommendations(Long studentId) {
        StudentProfile student = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Opportunity> allOpportunities = opportunityRepository.findAll();
        List<RecommendationDTO> recommendations = new ArrayList<>();

        Set<String> studentSkills = student.getSkills().stream().map(Skill::getName).collect(Collectors.toSet());
        Set<String> studentCategories = student.getPreferredCategories().stream().map(Category::getName).collect(Collectors.toSet());

        for (Opportunity opp : allOpportunities) {
            int score = 0;

            // 1. Score by Category Match (High Weight: +5 points)
            if (studentCategories.contains(opp.getCategory().getName())) {
                score += 5;
            }

            // 2. Score by Skills Match (+3 points per matching skill)
            Set<String> requiredSkills = opp.getSkillsRequired().stream().map(Skill::getName).collect(Collectors.toSet());
            for (String required : requiredSkills) {
                if (studentSkills.contains(required)) {
                    score += 3;
                }
            }

            // Only recommend if there is at least some match
            if (score > 0) {
                recommendations.add(new RecommendationDTO(mapToDTO(opp), score));
            }
        }

        // Sort by match score in descending order
        recommendations.sort(Comparator.comparingInt(RecommendationDTO::getMatchScore).reversed());

        return recommendations;
    }

    // Reuse mapping logic (In a real app, this would be a shared Mapper component)
    private OpportunityDTO mapToDTO(Opportunity opportunity) {
        OpportunityDTO dto = new OpportunityDTO();
        dto.setId(opportunity.getId());
        dto.setTitle(opportunity.getTitle());
        dto.setOrganization(opportunity.getOrganization());
        dto.setDescription(opportunity.getDescription());
        dto.setCategory(opportunity.getCategory().getName());
        
        Set<String> skillNames = opportunity.getSkillsRequired().stream()
                .map(Skill::getName)
                .collect(Collectors.toSet());
        dto.setSkillsRequired(skillNames);
        
        dto.setEligibility(opportunity.getEligibility());
        dto.setLocation(opportunity.getLocation());
        dto.setMode(opportunity.getMode());
        dto.setStartDate(opportunity.getStartDate());
        dto.setDeadline(opportunity.getDeadline());
        dto.setStipend(opportunity.getStipend());
        dto.setFee(opportunity.getFee());
        dto.setApplicationUrl(opportunity.getApplicationUrl());
        dto.setImageUrl(opportunity.getImageUrl());
        dto.setStatus(opportunity.getStatus());
        dto.setCreatedAt(opportunity.getCreatedAt());
        return dto;
    }
}
