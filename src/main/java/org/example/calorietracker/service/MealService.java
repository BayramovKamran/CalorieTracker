package org.example.calorietracker.service;

import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.model.Food;
import org.example.calorietracker.model.Meal;
import org.example.calorietracker.model.User;
import org.example.calorietracker.repository.FoodRepository;
import org.example.calorietracker.repository.MealRepository;
import org.example.calorietracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {

    private final MealRepository mealRepository;

    private final UserRepository userRepository;

    private final FoodRepository foodRepository;

    public MealService(MealRepository mealRepository, UserRepository userRepository, FoodRepository foodRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
    }

    public Meal addMeal(Long userId, List<Long> foodIds, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Food> foods = foodIds.stream()
                .map(id -> foodRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + id)))
                .collect(Collectors.toList());

        Meal meal = new Meal();
        meal.setUser(user);
        meal.setFoods(foods);
        meal.setDate(date);

        return mealRepository.save(meal);
    }

    public List<Meal> getMealsForDay(Long userId, LocalDate date) {
        return mealRepository.findByUserIdAndDate(userId, date);
    }

    public List<Meal> getMealHistory(Long userId) {
        return mealRepository.findByUserId(userId);
    }

    public double getTotalCaloriesForDay(Long userId, LocalDate date) {
        List<Meal> meals = getMealsForDay(userId, date);
        return meals.stream()
                .flatMap(meal -> meal.getFoods().stream())
                .mapToInt(Food::getCaloriesPerServing)
                .sum();
    }

    public boolean checkCalorieLimit(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        double totalCalories = getTotalCaloriesForDay(userId, date);
        return totalCalories <= user.getDailyCalorieGoal();
    }
}
