package com.recipe.manageRecipe.controller;

import com.recipe.manageRecipe.entity.Recipe;
import com.recipe.manageRecipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeService recipeService;

    //api -2 searching
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchRecipes(
            @RequestParam(required = false) String caloriesOperation,
            @RequestParam(required = false) Integer calories,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Integer totalTime,
            @RequestParam(required = false) Float rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Recipe> recipePage = recipeService.searchRecipes(
                caloriesOperation, calories, title, cuisine, totalTime, rating,
                PageRequest.of(page, size, Sort.by("rating").descending())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("data", recipePage.getContent());
        response.put("page", page);
        response.put("size", size);
        response.put("total", recipePage.getTotalElements());

        return ResponseEntity.ok(response);
    }
//api -1 pagination
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRecipes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        // Convert from 1-based to 0-based page indexing
        Page<Recipe> recipePage = recipeService.findAll(
                PageRequest.of(page - 1, limit, Sort.by("rating").descending())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("page", page);
        response.put("limit", limit);
        response.put("total", recipePage.getTotalElements());
        response.put("data", recipePage.getContent());

        return ResponseEntity.ok(response);
    }





}