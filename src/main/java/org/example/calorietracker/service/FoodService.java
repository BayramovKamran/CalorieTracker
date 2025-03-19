package org.example.calorietracker.service;


import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.model.Food;
import org.example.calorietracker.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Food createFood(Food food) {
        return foodRepository.save(food);
    }

    public Food getFood(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + id));
    }

    public Food updateFood(Long id, Food foodDetails) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + id));

        food.setName(foodDetails.getName());
        food.setCaloriesPerServing(foodDetails.getCaloriesPerServing());
        food.setProtein(foodDetails.getProtein());
        food.setFats(foodDetails.getFats());
        food.setCarbs(foodDetails.getCarbs());

        return foodRepository.save(food);
    }

    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + id));
        foodRepository.delete(food);
    }

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
}
