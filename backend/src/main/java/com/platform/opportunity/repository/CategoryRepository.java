package com.platform.opportunity.repository;

import com.platform.opportunity.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
