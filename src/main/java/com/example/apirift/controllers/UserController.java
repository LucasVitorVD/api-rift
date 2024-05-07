package com.example.apirift.controllers;

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
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = service.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        UserDTO user = service.findById(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO data) {
        UserDTO savedUser = service.save(data);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(data.getId()).toUri();

        return ResponseEntity.created(uri).body(savedUser);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO updatedData) {
        UserDTO updatedUser = service.update(updatedData);

        return ResponseEntity.ok(updatedUser);
    }
}