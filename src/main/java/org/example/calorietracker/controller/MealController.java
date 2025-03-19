package org.example.calorietracker.controller;

import org.example.calorietracker.model.Meal;
import org.example.calorietracker.service.MealService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<Meal> addMeal(
            @PathVariable Long userId,
            @RequestParam List<Long> foodIds,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Meal meal = mealService.addMeal(userId, foodIds, date);
        return new ResponseEntity<>(meal, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/day/{date}")
    public ResponseEntity<List<Meal>> getMealsForDay(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Meal> meals = mealService.getMealsForDay(userId, date);
        return new ResponseEntity<>(meals, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/history")
    public ResponseEntity<List<Meal>> getMealHistory(@PathVariable Long userId) {
        List<Meal> meals = mealService.getMealHistory(userId);
        return new ResponseEntity<>(meals, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/day/{date}/total-calories")
    public ResponseEntity<Double> getTotalCaloriesForDay(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        double totalCalories = mealService.getTotalCaloriesForDay(userId, date);
        return new ResponseEntity<>(totalCalories, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/day/{date}/check-limit")
    public ResponseEntity<Boolean> checkCalorieLimit(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        boolean withinLimit = mealService.checkCalorieLimit(userId, date);
        return new ResponseEntity<>(withinLimit, HttpStatus.OK);
    }
}
