package com.example.apirift.controllers;

import com.example.apirift.entitiesDTO.CategoryDTO;
import com.example.apirift.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories = service.findAll();

        return ResponseEntity.ok(categories);
    }

    @GetMapping(params = "category")
    public ResponseEntity<CategoryDTO> findCategoryByName(@RequestParam(name = "category") String categoryName) {
        CategoryDTO category = service.findByName(categoryName);

        return ResponseEntity.ok(category);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        CategoryDTO category = service.findById(id);

        return ResponseEntity.ok(category);
    }
}