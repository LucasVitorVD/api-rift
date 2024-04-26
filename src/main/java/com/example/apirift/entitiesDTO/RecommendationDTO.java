package com.example.apirift.entitiesDTO;

public record RecommendationDTO(Long id, Long userId, Long categoryId, String title, String description, String previewUrl, String img) {}