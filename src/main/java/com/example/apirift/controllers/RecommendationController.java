package com.example.apirift.controllers;

import com.example.apirift.entities.Recommendation;
import com.example.apirift.entitiesDTO.RecommendationDTO;
import com.example.apirift.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService service;

    @GetMapping
    public ResponseEntity<List<Recommendation>> getRecommendations() {
        List<Recommendation> recommendations = service.findAll();

        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recommendation> getRecommendation(@PathVariable Long id) {
        Recommendation recommendation = service.findById(id);

        return ResponseEntity.ok(recommendation);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Recommendation> addRecommendation(@RequestBody RecommendationDTO data) {
        Recommendation newRecommendation = service.save(data);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newRecommendation.getId()).toUri();

        return ResponseEntity.created(uri).body(newRecommendation);
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Recommendation> updateRecommendation(@RequestBody RecommendationDTO updatedData) {
        Recommendation updatedRecommendation = service.update(updatedData);

        return ResponseEntity.ok(updatedRecommendation);
    }
}