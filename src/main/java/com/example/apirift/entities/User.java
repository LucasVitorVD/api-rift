package com.example.apirift.entities;

import com.example.apirift.entitiesDTO.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @OneToMany(mappedBy = "user")
    private Set<Recommendation> recommendations = new HashSet<>();

    public User(UserDTO data) {
        this.username = data.username();
        this.email = data.email();
        this.fullName = data.fullName();
        this.profileImageUrl = data.profileImageUrl();
    }
}
