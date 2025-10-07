package com.recipe.manageRecipe.config;

import com.recipe.manageRecipe.service.RecipeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RecipeDataLoader implements CommandLineRunner {

    private final RecipeService recipeService;

    public RecipeDataLoader(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public void run(String... args) {
        recipeService.loadRecipesFromJson();
    }
}
