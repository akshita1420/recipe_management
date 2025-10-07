package com.recipe.manageRecipe.controller;

import com.recipe.manageRecipe.entity.Recipe;
import com.recipe.manageRecipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeViewController {

    private final RecipeService recipeService;

    @GetMapping
    public String listRecipes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Float rating,
            Model model) {

        Page<Recipe> recipePage = recipeService.searchRecipes(
                null,
                null,
                title,
                cuisine,
                null,
                rating,
                PageRequest.of(page - 1, size, Sort.by("rating").descending())
        );

        model.addAttribute("recipes", recipePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", recipePage.getTotalPages());
        model.addAttribute("totalItems", recipePage.getTotalElements());

        return "recipes/list";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Recipe getRecipeDetails(@PathVariable Long id) {
        return recipeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "recipes/error";
    }
}