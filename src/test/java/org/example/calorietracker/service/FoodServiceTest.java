package org.example.calorietracker.service;

import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.model.Food;
import org.example.calorietracker.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private FoodService foodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFood_shouldSaveFood() {
        Food food = new Food();
        food.setName("Apple");
        food.setCaloriesPerServing(50);

        when(foodRepository.save(any(Food.class))).thenReturn(food);

        Food createdFood = foodService.createFood(food);

        assertNotNull(createdFood);
        assertEquals("Apple", createdFood.getName());
        assertEquals(50, createdFood.getCaloriesPerServing());
    }

    @Test
    void getFood_shouldReturnFoodById() {
        Long foodId = 1L;
        Food food = new Food();
        food.setId(foodId);
        food.setName("Banana");
        food.setCaloriesPerServing(100);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));

        Food retrievedFood = foodService.getFood(foodId);

        assertNotNull(retrievedFood);
        assertEquals(foodId, retrievedFood.getId());
        assertEquals("Banana", retrievedFood.getName());
    }

    @Test
    void getFood_shouldThrowExceptionWhenFoodNotFound() {
        Long foodId = 1L;

        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> foodService.getFood(foodId));
    }

}
