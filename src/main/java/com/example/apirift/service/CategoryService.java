package com.example.apirift.service;

import com.example.apirift.entities.Category;
import com.example.apirift.entitiesDTO.CategoryDTO;
import com.example.apirift.entitiesDTO.RecommendationDTO;
import com.example.apirift.repositories.CategoryRepository;
import com.example.apirift.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<CategoryDTO> findAll() {
        List<Category> categories = repository.findAll();

        if (categories.isEmpty()) {
            return Collections.emptyList();
        }

        return categories.stream().map(this::convertToDTO).toList();
    }

    public CategoryDTO findById(Long id) {
        Optional<Category> categoryOptional = repository.findById(id);

        if (categoryOptional.isPresent()) {
            return convertToDTO(categoryOptional.get());
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();

        dto.setId(category.getId());
        dto.setName(category.getName());

        category.getRecommendations().forEach(recommendation -> {
            RecommendationDTO recommendationDTO = new RecommendationDTO();

            recommendationDTO.setId(recommendation.getId());
            recommendationDTO.setName(recommendation.getName());
            recommendationDTO.setDescription(recommendation.getDescription());
            recommendationDTO.setPersonalComment(recommendation.getPersonalComment());
            recommendationDTO.setImage(recommendation.getImage());
            recommendationDTO.setPreviewUrl(recommendation.getPreviewUrl());
            recommendationDTO.setCategoryId(recommendation.getCategory().getId());
            recommendationDTO.setUserId(recommendation.getUser().getId());

            dto.getRecommendations().add(recommendationDTO);
        });

        return dto;
    }
}
