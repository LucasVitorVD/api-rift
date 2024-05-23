package com.example.apirift.repositories;

import com.example.apirift.entities.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long>, PagingAndSortingRepository<Recommendation, Long> {
}