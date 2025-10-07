package com.recipe.manageRecipe.specification;

import com.recipe.manageRecipe.entity.Recipe;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RecipeSpecification {

    public static Specification<Recipe> searchRecipes(
            String caloriesOperation,
            Integer calories,
            String title,
            String cuisine,
            Integer totalTime,
            Float rating) {

        return (Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (calories != null && caloriesOperation != null) {
                String caloriesStr = calories + " kcal";
                // extract calories from JSONB
                switch (caloriesOperation) {
                    case "<" -> predicates.add(cb.lessThan(
                            root.get("nutrients").as(String.class),
                            String.format("{\"%s\": \"%s\"}", "calories", caloriesStr)
                    ));
                    case ">" -> predicates.add(cb.greaterThan(
                            root.get("nutrients").as(String.class),
                            String.format("{\"%s\": \"%s\"}", "calories", caloriesStr)
                    ));
                    case "=" -> predicates.add(cb.equal(
                            root.get("nutrients").as(String.class),
                            String.format("{\"%s\": \"%s\"}", "calories", caloriesStr)
                    ));
                }
            }

            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                ));
            }

            if (cuisine != null && !cuisine.isEmpty()) {
                predicates.add(cb.equal(root.get("cuisine"), cuisine));
            }

            if (totalTime != null) {
                predicates.add(cb.equal(root.get("total_time"), totalTime));
            }

            if (rating != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), rating));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}