package com.platform.opportunity.service;

import com.platform.opportunity.dto.BookmarkDTO;
import com.platform.opportunity.dto.OpportunityDTO;
import com.platform.opportunity.model.Bookmark;
import com.platform.opportunity.model.Opportunity;
import com.platform.opportunity.model.Skill;
import com.platform.opportunity.model.StudentProfile;
import com.platform.opportunity.repository.BookmarkRepository;
import com.platform.opportunity.repository.OpportunityRepository;
import com.platform.opportunity.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Transactional(readOnly = true)
    public List<BookmarkDTO> getStudentBookmarks(Long studentId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByStudent_IdOrderBySavedAtDesc(studentId);
        return bookmarks.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public BookmarkDTO addBookmark(Long studentId, Long opportunityId) {
        if (bookmarkRepository.existsByStudent_IdAndOpportunity_Id(studentId, opportunityId)) {
            throw new RuntimeException("Opportunity is already bookmarked");
        }

        StudentProfile student = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        Bookmark bookmark = new Bookmark();
        bookmark.setStudent(student);
        bookmark.setOpportunity(opportunity);
        
        bookmark = bookmarkRepository.save(bookmark);
        return mapToDTO(bookmark);
    }

    @Transactional
    public void removeBookmark(Long studentId, Long opportunityId) {
        Bookmark bookmark = bookmarkRepository.findByStudent_IdAndOpportunity_Id(studentId, opportunityId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));
        bookmarkRepository.delete(bookmark);
    }

    private BookmarkDTO mapToDTO(Bookmark bookmark) {
        BookmarkDTO dto = new BookmarkDTO();
        dto.setId(bookmark.getId());
        dto.setSavedAt(bookmark.getSavedAt());
        
        // Map inner OpportunityDTO
        Opportunity opp = bookmark.getOpportunity();
        OpportunityDTO oppDto = new OpportunityDTO();
        oppDto.setId(opp.getId());
        oppDto.setTitle(opp.getTitle());
        oppDto.setOrganization(opp.getOrganization());
        oppDto.setDescription(opp.getDescription());
        oppDto.setCategory(opp.getCategory().getName());
        
        Set<String> skillNames = opp.getSkillsRequired().stream()
                .map(Skill::getName)
                .collect(Collectors.toSet());
        oppDto.setSkillsRequired(skillNames);
        
        oppDto.setEligibility(opp.getEligibility());
        oppDto.setLocation(opp.getLocation());
        oppDto.setMode(opp.getMode());
        oppDto.setStartDate(opp.getStartDate());
        oppDto.setDeadline(opp.getDeadline());
        oppDto.setStipend(opp.getStipend());
        oppDto.setFee(opp.getFee());
        oppDto.setApplicationUrl(opp.getApplicationUrl());
        oppDto.setImageUrl(opp.getImageUrl());
        oppDto.setStatus(opp.getStatus());
        oppDto.setCreatedAt(opp.getCreatedAt());
        
        dto.setOpportunity(oppDto);
        return dto;
    }
}
