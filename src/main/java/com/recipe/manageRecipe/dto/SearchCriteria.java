package com.recipe.manageRecipe.dto;

import lombok.Data;

@Data
public class SearchCriteria {
    private String caloriesOperation;
    private Integer calories;
    private String title;
    private String cuisine;
    private Integer totalTime;
    private Float rating;
}