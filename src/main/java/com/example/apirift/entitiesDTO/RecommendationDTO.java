package com.example.apirift.entitiesDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecommendationDTO {
    private Long id;
    private String userId;
    private Long categoryId;
    private String name;
    private String description;
    private String personalComment;
    private String previewUrl;
    private String image;
}