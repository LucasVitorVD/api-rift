package com.example.apirift.service;

import com.example.apirift.entities.Category;
import com.example.apirift.entities.User;
import com.example.apirift.entitiesDTO.CategoryDTO;
import com.example.apirift.entitiesDTO.RecommendationDTO;
import com.example.apirift.entitiesDTO.UserDTO;
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
public class UserService {
    @Autowired
    private UserRepository repository;

    public List<UserDTO> findAll() {
        List<User> users = repository.findAll();

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream().map(this::convertToDTO).toList();
    }

    public UserDTO findById(String id) {
        Optional<User> user = repository.findById(id);

        if (user.isPresent()) {
            return convertToDTO(user.get());
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    public UserDTO save(UserDTO data) {
        User newUser = new User();

        newUser.setId(data.getId());
        newUser.setName(data.getName());
        newUser.setEmail(data.getEmail());
        newUser.setPicture(data.getPicture());

        User savedUser = repository.save(newUser);

        return convertToDTO(savedUser);
    }

    public void delete(String id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        if (!user.getRecommendations().isEmpty()) {
            throw new DatabaseException("Cannot delete the user because there are recommendations associated with it.");
        }

        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException err) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException err) {
            throw new DatabaseException(err.getMessage());
        }
    }

    public UserDTO update(UserDTO updatedData) {
        try {
            User user = repository.getReferenceById(updatedData.getId());

            User savedUser = updateData(user, updatedData);

            return convertToDTO(savedUser);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(updatedData.getId());
        }
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPicture(user.getPicture());

        user.getRecommendations().forEach(recommendation -> {
            RecommendationDTO recommendationDTO = new RecommendationDTO();

            recommendationDTO.setId(recommendation.getId());
            recommendationDTO.setName(recommendation.getName());
            recommendationDTO.setDescription(recommendation.getDescription());
            recommendationDTO.setPersonalComment(recommendation.getPersonalComment());
            recommendationDTO.setImage(recommendation.getImage());
            recommendationDTO.setPreviewUrl(recommendation.getPreviewUrl());
            recommendationDTO.setCategoryId(recommendation.getCategory().getId());
            recommendationDTO.setUserId(recommendation.getUser().getId());

            dto.getRecommendationDTOS().add(recommendationDTO);
        });

        return dto;
    }

    private User updateData(User entity, UserDTO obj) {
        entity.setName(obj.getName());
        entity.setEmail(obj.getEmail());
        entity.setPicture(obj.getPicture());

        return repository.save(entity);
    }
}
