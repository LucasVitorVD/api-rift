package com.example.apirift.entitiesDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String picture;

    private List<RecommendationDTO> recommendations = new ArrayList<>();
}
