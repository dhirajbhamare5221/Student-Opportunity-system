package com.platform.opportunity.service;

import com.platform.opportunity.dto.OpportunityDTO;
import com.platform.opportunity.dto.OpportunityRequest;
import com.platform.opportunity.model.Category;
import com.platform.opportunity.model.Opportunity;
import com.platform.opportunity.model.Skill;
import com.platform.opportunity.repository.CategoryRepository;
import com.platform.opportunity.repository.OpportunityRepository;
import com.platform.opportunity.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OpportunityService {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Transactional(readOnly = true)
    public Page<OpportunityDTO> getOpportunities(String categoryName, String keyword, Pageable pageable) {
        Page<Opportunity> opportunities;
        
        boolean hasCategory = categoryName != null && !categoryName.isEmpty();
        boolean hasKeyword = keyword != null && !keyword.isEmpty();

        if (hasCategory && hasKeyword) {
            opportunities = opportunityRepository.findByCategory_NameAndTitleContainingIgnoreCaseOrCategory_NameAndOrganizationContainingIgnoreCase(
                    categoryName, keyword, categoryName, keyword, pageable);
        } else if (hasCategory) {
            opportunities = opportunityRepository.findByCategory_Name(categoryName, pageable);
        } else if (hasKeyword) {
            opportunities = opportunityRepository.findByTitleContainingIgnoreCaseOrOrganizationContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            opportunities = opportunityRepository.findAll(pageable);
        }
        
        return opportunities.map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public OpportunityDTO getOpportunityById(Long id) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));
        return mapToDTO(opportunity);
    }

    @Transactional
    public OpportunityDTO createOpportunity(OpportunityRequest request) {
        Opportunity opportunity = new Opportunity();
        opportunity.setTitle(request.getTitle());
        opportunity.setOrganization(request.getOrganization());
        opportunity.setDescription(request.getDescription());
        opportunity.setEligibility(request.getEligibility());
        opportunity.setLocation(request.getLocation());
        opportunity.setMode(request.getMode());
        opportunity.setStartDate(request.getStartDate());
        opportunity.setDeadline(request.getDeadline());
        opportunity.setStipend(request.getStipend());
        opportunity.setFee(request.getFee());
        opportunity.setApplicationUrl(request.getApplicationUrl());
        opportunity.setImageUrl(request.getImageUrl());
        opportunity.setStatus(Opportunity.Status.ACTIVE);

        // Map Category
        Category category = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals(request.getCategory()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategory()));
        opportunity.setCategory(category);

        // Map Skills
        if (request.getSkillsRequired() != null && !request.getSkillsRequired().isEmpty()) {
            List<Skill> allSkills = skillRepository.findAll();
            Set<Skill> requiredSkills = allSkills.stream()
                    .filter(s -> request.getSkillsRequired().contains(s.getName()))
                    .collect(Collectors.toSet());
            opportunity.setSkillsRequired(requiredSkills);
        }

        opportunity = opportunityRepository.save(opportunity);
        return mapToDTO(opportunity);
    }

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
