package com.example.backend.repository;


import com.example.backend.domain.video.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.categoryName = :categoryName")
    Optional<Category> findCategoryByCategoryName(String categoryName);
}
