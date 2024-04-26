package com.example.apirift.entities;

import com.example.apirift.entitiesDTO.RecommendationDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommendations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(name = "preview_url")
    private String previewUrl;

    private String img;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    public Recommendation(RecommendationDTO data) {
        this.id = data.id();
        this.title = data.title();
        this.description = data.description();
        this.previewUrl = data.previewUrl();
        this.img = data.previewUrl();
    }
}
