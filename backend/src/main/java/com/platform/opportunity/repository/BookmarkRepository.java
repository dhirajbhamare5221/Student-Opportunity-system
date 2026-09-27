package com.platform.opportunity.repository;

import com.platform.opportunity.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByStudent_IdOrderBySavedAtDesc(Long studentId);
    boolean existsByStudent_IdAndOpportunity_Id(Long studentId, Long opportunityId);
    Optional<Bookmark> findByStudent_IdAndOpportunity_Id(Long studentId, Long opportunityId);
}
