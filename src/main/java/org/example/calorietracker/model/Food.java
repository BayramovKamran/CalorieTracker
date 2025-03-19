package org.example.calorietracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Table(name = "foods")
@Data
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Food name is required")
    @Column(name = "name")
    private String name;

    @Positive(message = "Calories per serving must be positive")
    @Column(name = "calories_per_serving")
    private int caloriesPerServing;

    @Positive(message = "Protein must be positive")
    @Column(name = "protein")
    private double protein;

    @Positive(message = "Fats must be positive")
    @Column(name = "fats")
    private double fats;

    @Positive(message = "Carbs must be positive")
    @Column(name = "carbs")
    private double carbs;
}
