package com.example.apirift.controllers;

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
    public ResponseEntity<List<RecommendationDTO>> getRecommendations() {
        List<RecommendationDTO> recommendations = service.findAll();

        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommendationDTO> getRecommendation(@PathVariable Long id) {
        RecommendationDTO recommendation = service.findById(id);

        return ResponseEntity.ok(recommendation);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<RecommendationDTO> addRecommendation(@RequestBody RecommendationDTO data) {
        RecommendationDTO newRecommendation = service.save(data);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newRecommendation.getId()).toUri();

        return ResponseEntity.created(uri).body(newRecommendation);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<RecommendationDTO> updateRecommendation(@PathVariable Long id, @RequestBody RecommendationDTO updatedData) {
        RecommendationDTO updatedRecommendation = service.update(id, updatedData);

        return ResponseEntity.ok(updatedRecommendation);
    }
}