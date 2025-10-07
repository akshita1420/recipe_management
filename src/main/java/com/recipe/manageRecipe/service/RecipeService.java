package com.recipe.manageRecipe.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import com.recipe.manageRecipe.entity.Recipe;
import com.recipe.manageRecipe.repository.RecipeRepository;
import com.recipe.manageRecipe.specification.RecipeSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Pageable;

@Service
public class RecipeService {

    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    // loading recipes
    public void loadRecipesFromJson() {
        String resourcePath = "/US_recipes_null.json";

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                logger.error("JSON file not found in resources folder: {}", resourcePath);
                return;
            }

            // parsing JSON
            Map<String, JsonNode> recipesMap = objectMapper.readValue(inputStream, new TypeReference<>() {});
            logger.info("Loading {} recipes from JSON", recipesMap.size());

            int batchSize = 50;
            int count = 0;

            for (Map.Entry<String, JsonNode> entry : recipesMap.entrySet()) {
                JsonNode recipeNode = entry.getValue();


                Recipe recipe = Recipe.builder()
                        .cuisine(getStringValue(recipeNode, "cuisine"))
                        .title(getStringValue(recipeNode, "title"))
                        .rating(getFloatValue(recipeNode, "rating"))
                        .prep_time(getIntegerValue(recipeNode, "prep_time"))
                        .cook_time(getIntegerValue(recipeNode, "cook_time"))
                        .total_time(getIntegerValue(recipeNode, "total_time"))
                        .description(getStringValue(recipeNode, "description"))
                        .serves(getStringValue(recipeNode, "serves"))
                        .build();

                // nutrients
                JsonNode nutrientsNode = recipeNode.get("nutrients");
                if (nutrientsNode != null && !nutrientsNode.isNull()) {
                    Map<String, Object> nutrientsMap = objectMapper.convertValue(nutrientsNode, Map.class);
                    recipe.setNutrients(nutrientsMap);
                }

                try {
                    recipeRepository.save(recipe);
                } catch (Exception e) {
                    logger.error("Error saving recipe: {}", recipe.getTitle(), e);
                }

                // Flush in batches to avoid memory overflow
                if (++count % batchSize == 0) {
                    recipeRepository.flush();
                    logger.info("Flushed {} recipes to the database", count);
                }
            }

            recipeRepository.flush();
            logger.info("Successfully loaded all recipes into the database");

        } catch (IOException e) {
            logger.error("Error loading recipes from JSON file: {}", resourcePath, e);
        }
    }


    private String getStringValue(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value != null && !value.isNull()) ? value.asText() : null;
    }

    private Float getFloatValue(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value != null && !value.isNull()) {
            try {
                return Float.parseFloat(value.asText());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Integer getIntegerValue(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value != null && !value.isNull()) {
            try {
                return Integer.parseInt(value.asText());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }


// filters
    public Page<Recipe> searchRecipes(
            String caloriesOperation,
            Integer calories,
            String title,
            String cuisine,
            Integer totalTime,
            Float rating,
            PageRequest pageRequest) {

        Specification<Recipe> spec = RecipeSpecification.searchRecipes(
                caloriesOperation, calories, title, cuisine, totalTime, rating
        );

        return recipeRepository.findAll(spec, pageRequest);
    }

    public Page<Recipe> findAll(Pageable pageable) {

        return recipeRepository.findAll(pageable);
    }
    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }
}
