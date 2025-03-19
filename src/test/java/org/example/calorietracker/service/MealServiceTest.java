package org.example.calorietracker.service;

import org.example.calorietracker.model.Food;
import org.example.calorietracker.model.Meal;
import org.example.calorietracker.model.User;
import org.example.calorietracker.repository.FoodRepository;
import org.example.calorietracker.repository.MealRepository;
import org.example.calorietracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private MealService mealService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMeal_shouldAddMealForUser() {
        Long userId = 1L;
        Long foodId1 = 1L;
        Long foodId2 = 2L;
        LocalDate date = LocalDate.now();

        User user = new User();
        user.setId(userId);

        Food food1 = new Food();
        food1.setId(foodId1);
        food1.setCaloriesPerServing(100);
        Food food2 = new Food();
        food2.setId(foodId2);
        food2.setCaloriesPerServing(200);


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(foodRepository.findById(foodId1)).thenReturn(Optional.of(food1));
        when(foodRepository.findById(foodId2)).thenReturn(Optional.of(food2));
        when(mealRepository.save(any(Meal.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved meal

        Meal meal = mealService.addMeal(userId, Arrays.asList(foodId1, foodId2), date);

        assertNotNull(meal);
        assertEquals(user, meal.getUser());
        assertEquals(2, meal.getFoods().size());
        assertEquals(date, meal.getDate());

    }

    @Test
    void getTotalCaloriesForDay_shouldCalculateTotalCalories() {
        Long userId = 1L;
        LocalDate date = LocalDate.now();

        User user = new User();
        user.setId(userId);


        Food food1 = new Food();
        food1.setId(1L);
        food1.setCaloriesPerServing(100);

        Food food2 = new Food();
        food2.setId(2L);
        food2.setCaloriesPerServing(200);

        Meal meal1 = new Meal();
        meal1.setId(1L);
        meal1.setUser(user);
        meal1.setFoods(Arrays.asList(food1));
        meal1.setDate(date);

        Meal meal2 = new Meal();
        meal2.setId(2L);
        meal2.setUser(user);
        meal2.setFoods(Arrays.asList(food2));
        meal2.setDate(date);

        when(mealRepository.findByUserIdAndDate(userId, date)).thenReturn(Arrays.asList(meal1, meal2));

        double totalCalories = mealService.getTotalCaloriesForDay(userId, date);

        assertEquals(300, totalCalories);
    }

    @Test
    void checkCalorieLimit_shouldReturnTrueWhenWithinLimit() {
        Long userId = 1L;
        LocalDate date = LocalDate.now();

        User user = new User();
        user.setId(userId);
        user.setDailyCalorieGoal(2000); // Set a daily calorie goal

        Food food1 = new Food();
        food1.setId(1L);
        food1.setCaloriesPerServing(500);

        Meal meal1 = new Meal();
        meal1.setId(1L);
        meal1.setUser(user);
        meal1.setFoods(Arrays.asList(food1));
        meal1.setDate(date);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mealRepository.findByUserIdAndDate(userId, date)).thenReturn(Arrays.asList(meal1));

        boolean withinLimit = mealService.checkCalorieLimit(userId, date);

        assertTrue(withinLimit);
    }

    @Test
    void checkCalorieLimit_shouldReturnFalseWhenOverLimit() {
        Long userId = 1L;
        LocalDate date = LocalDate.now();

        User user = new User();
        user.setId(userId);
        user.setDailyCalorieGoal(1000); // Set a daily calorie goal

        Food food1 = new Food();
        food1.setId(1L);
        food1.setCaloriesPerServing(1500);

        Meal meal1 = new Meal();
        meal1.setId(1L);
        meal1.setUser(user);
        meal1.setFoods(Arrays.asList(food1));
        meal1.setDate(date);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mealRepository.findByUserIdAndDate(userId, date)).thenReturn(Arrays.asList(meal1));

        boolean withinLimit = mealService.checkCalorieLimit(userId, date);

        assertFalse(withinLimit);
    }
}
