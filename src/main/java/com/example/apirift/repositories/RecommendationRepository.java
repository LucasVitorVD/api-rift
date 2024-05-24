package com.example.apirift.repositories;

import com.example.apirift.entities.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long>, PagingAndSortingRepository<Recommendation, Long> {
    @Query("SELECT r FROM Recommendation r WHERE r.category.id = :categoryId ORDER BY r.createdAt DESC")
    List<Recommendation> findInitialRecommendationsByCategory(@Param("categoryId") Long categoryId);
}