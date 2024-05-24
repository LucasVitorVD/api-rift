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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<RecommendationDTO> findAll(Pageable pageable) {
        Page<Recommendation> recommendations = repository.findAll(pageable);

        return recommendations.map(this::convertToDTO);
    }

    public List<RecommendationDTO> findRecommendationsByCategory(String categoryName, Pageable pageable) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException(categoryName));

        List<Recommendation> recommendations = repository.findAll(pageable).getContent()
                .stream()
                .filter(recommendation -> recommendation.getCategory().getId().equals(category.getId()))
                .toList();

        return recommendations.stream().map(this::convertToDTO).toList();
    }

    public List<RecommendationDTO> findUserRecommendations(String userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId));

        List<Recommendation> recommendations = repository.findAll(pageable).getContent()
                .stream()
                .filter(recommendation -> recommendation.getUser().getId().equals(user.getId()))
                .toList();

        return recommendations.stream().map(this::convertToDTO).toList();
    }

    public RecommendationDTO findById(Long id) {
        Recommendation recommendation = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        return convertToDTO(recommendation);
    }

    public RecommendationDTO save(RecommendationDTO data) {
        try {
            User user = userRepository.findById(data.getUserId())
                    .orElseThrow(() -> new DatabaseException("User not found with ID: " + data.getUserId()));

            Category category = categoryRepository.findById(data.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(data.getCategoryId()));

            Recommendation newRecommendation = new Recommendation();
            newRecommendation.setName(data.getName());
            newRecommendation.setDescription(data.getDescription());
            newRecommendation.setImage(data.getImage());
            newRecommendation.setPreviewUrl(data.getPreviewUrl());
            newRecommendation.setPersonalComment(data.getPersonalComment());
            newRecommendation.setUser(user);
            newRecommendation.setCategory(category);

            return convertToDTO(repository.save(newRecommendation));
        } catch (DatabaseException err) {
            throw new DatabaseException(err.getMessage());
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

    public RecommendationDTO update(RecommendationDTO updatedData) {
        try {
            Recommendation referenceRecommendation = repository.findById(updatedData.getId()).
                    orElseThrow(() -> new ResourceNotFoundException(updatedData.getId()));

            Category category = categoryRepository.getReferenceById(updatedData.getCategoryId());


            Recommendation updatedRecommendation = updateData(referenceRecommendation, updatedData, category);

            return convertToDTO(updatedRecommendation);
        } catch (ResourceNotFoundException err) {
            throw new ResourceNotFoundException(err.getMessage());
        }
    }

    private RecommendationDTO convertToDTO(Recommendation recommendation) {
        return new RecommendationDTO(
                recommendation.getId(),
                recommendation.getName(),
                recommendation.getDescription(),
                recommendation.getImage(),
                recommendation.getPersonalComment(),
                recommendation.getPreviewUrl(),
                recommendation.getUser().getId(),
                recommendation.getCategory().getId(),
                recommendation.getCreatedAt(),
                recommendation.getUpdatedAt()
        );
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
