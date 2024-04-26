package com.example.apirift.controllers;

import com.example.apirift.entities.User;
import com.example.apirift.entitiesDTO.UserDTO;
import com.example.apirift.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = service.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = service.findById(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<User> createUser(@RequestBody UserDTO data) {
        User savedUser = service.save(data);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(data.id()).toUri();

        return ResponseEntity.created(uri).body(savedUser);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public User updateUser(@RequestBody UserDTO updatedData) {
        return service.update(updatedData);
    }
}