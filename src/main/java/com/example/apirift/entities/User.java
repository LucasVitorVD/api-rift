package com.example.apirift.entities;

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
    private String id;

    private String name;

    private String email;

    private String picture;

    @OneToMany(mappedBy = "user")
    private Set<Recommendation> recommendations = new HashSet<>();
}
