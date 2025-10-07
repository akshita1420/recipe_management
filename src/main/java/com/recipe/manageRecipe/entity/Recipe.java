package com.recipe.manageRecipe.entity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.Map;

@Data
@Entity
@Table(name ="recipe")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String cuisine;

    @Column
    private String title;

    @Column
    private Float rating;

    @Column
    private Integer prep_time;

    @Column
    private Integer cook_time;

    @Column
    private Integer total_time;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(columnDefinition="JSONB")
    @Type(JsonBinaryType.class)
    private Map<String, Object> nutrients;

    @Column
    private String serves;

}
