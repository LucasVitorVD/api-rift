package com.example.apirift.service;

import com.example.apirift.entities.User;
import com.example.apirift.entitiesDTO.UserDTO;
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
public class UserService {
    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        Optional<User> user = repository.findById(id);

        return user.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public User save(UserDTO data) {
        User newUser = new User(data);
        return repository.save(newUser);
    }

    public void delete(Long id) {
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

    public User update(UserDTO updatedData) {
        try {
            User user = repository.getReferenceById(updatedData.id());
            updateData(user, updatedData);
            return repository.save(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(updatedData.id());
        }
    }

    private void updateData(User entity, UserDTO obj) {
        entity.setUsername(obj.username());
        entity.setFullName(obj.fullName());
        entity.setEmail(obj.email());
        entity.setProfileImageUrl(obj.profileImageUrl());
    }
}
