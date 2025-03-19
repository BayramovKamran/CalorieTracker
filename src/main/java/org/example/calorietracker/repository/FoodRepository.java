package org.example.calorietracker.repository;

import org.example.calorietracker.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
