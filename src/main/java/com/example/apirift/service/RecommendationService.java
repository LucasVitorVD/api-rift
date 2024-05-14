package com.example.apirift.service;

import com.example.apirift.entities.Category;
import com.example.apirift.entities.Recommendation;
import com.example.apirift.entities.User;
import com.example.apirift.entitiesDTO.RecommendationDTO;
import com.example.apirift.repositories.CategoryRepository;
import com.example.apirift.repositories.RecommendationRepository;
import com.example.apirift.repositories.UserRepository;
import com.example.apirift.service.exceptions.DatabaseException;
import com.example.apirift.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<RecommendationDTO> findAll() {
        List<Recommendation> recommendations = repository.findAll();

        if (recommendations.isEmpty()) {
            return Collections.emptyList();
        }

        return recommendations.stream().map(this::convertToDTO).toList();
    }

    public RecommendationDTO findById(Long id) {
        Optional<Recommendation> recommendationOptional = repository.findById(id);

        if (recommendationOptional.isPresent()) {
            return convertToDTO(recommendationOptional.get());
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    public RecommendationDTO save(RecommendationDTO data) {
        try {
            User user = userRepository.findById(data.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + data.getUserId()));

            Category category = categoryRepository.findById(data.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + data.getCategoryId()));

            Recommendation newRecommendation = new Recommendation();

            newRecommendation.setName(data.getName());
            newRecommendation.setDescription(data.getDescription());
            newRecommendation.setImage(data.getImage());
            newRecommendation.setPreviewUrl(data.getPreviewUrl());
            newRecommendation.setPersonalComment(data.getPersonalComment());
            newRecommendation.setUser(user);
            newRecommendation.setCategory(category);

            Recommendation savedRecommendation = repository.save(newRecommendation);

            return convertToDTO(savedRecommendation);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(data.getUserId());
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public RecommendationDTO update(Long id, RecommendationDTO updatedData) {
        try {
            Recommendation referenceRecommendation = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
            Category category = categoryRepository.getReferenceById(updatedData.getCategoryId());

            Recommendation updatedRecommendation = updateData(referenceRecommendation, updatedData, category);

            return convertToDTO(updatedRecommendation);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private RecommendationDTO convertToDTO(Recommendation recommendation) {
        RecommendationDTO dto = new RecommendationDTO();
        dto.setId(recommendation.getId());
        dto.setName(recommendation.getName());
        dto.setDescription(recommendation.getDescription());
        dto.setImage(recommendation.getImage());
        dto.setPersonalComment(recommendation.getPersonalComment());
        dto.setPreviewUrl(recommendation.getPreviewUrl());
        dto.setUserId(recommendation.getUser().getId());
        dto.setCategoryId(recommendation.getCategory().getId());

        return dto;
    }

    private Recommendation updateData(Recommendation entity, RecommendationDTO obj, Category categoryReference) {
        entity.setName(obj.getName());
        entity.setImage(obj.getImage());
        entity.setCategory(categoryReference);
        entity.setDescription(obj.getDescription());
        entity.setPreviewUrl(obj.getPreviewUrl());
        entity.setPersonalComment(obj.getPersonalComment());

        return repository.save(entity);
    }
}
