package com.platform.opportunity.repository;

import com.platform.opportunity.model.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    Page<Opportunity> findByCategory_Name(String categoryName, Pageable pageable);
    Page<Opportunity> findByTitleContainingIgnoreCaseOrOrganizationContainingIgnoreCase(String title, String org, Pageable pageable);
    Page<Opportunity> findByCategory_NameAndTitleContainingIgnoreCaseOrCategory_NameAndOrganizationContainingIgnoreCase(String cat1, String title, String cat2, String org, Pageable pageable);
    Page<Opportunity> findAll(Pageable pageable);
}
