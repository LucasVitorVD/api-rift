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

    public List<Recommendation> findAll() {
        return repository.findAll();
    }

    public Recommendation findById(Long id) {
        Optional<Recommendation> user = repository.findById(id);

        return user.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Recommendation save(RecommendationDTO data) {
        try {
            User user = userRepository.getReferenceById(data.userId());
            Category category = categoryRepository.getReferenceById(data.categoryId());

            Recommendation newRecommendation = new Recommendation(data);

            newRecommendation.setUser(user);
            newRecommendation.setCategory(category);


            return repository.save(newRecommendation);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(data.userId());
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

    public Recommendation update(RecommendationDTO updatedData) {
        try {
            Recommendation referenceRecommendation = repository.findById(updatedData.id()).orElseThrow(() -> new ResourceNotFoundException(updatedData.id()));
            Category category = categoryRepository.getReferenceById(updatedData.categoryId());

            updateData(referenceRecommendation, updatedData, category);

            return referenceRecommendation;
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(updatedData.id());
        }
    }

    private void updateData(Recommendation entity, RecommendationDTO obj, Category categoryReference) {
        entity.setTitle(obj.title());
        entity.setImg(obj.img());
        entity.setCategory(categoryReference);
        entity.setDescription(obj.description());
        entity.setPreviewUrl(obj.previewUrl());
    }
}
