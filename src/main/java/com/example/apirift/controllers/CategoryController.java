package com.example.apirift.controllers;

import com.example.apirift.entities.Category;
import com.example.apirift.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = service.findAll();

        return ResponseEntity.ok(categories);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        Optional<Category> category = service.findById(id);

        return category.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}