package org.example.calorietracker.service;

import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.model.Food;
import org.example.calorietracker.model.User;
import org.example.calorietracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        calculateDailyCalorieGoal(user);
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setAge(userDetails.getAge());
        user.setWeight(userDetails.getWeight());
        user.setHeight(userDetails.getHeight());
        user.setGoal(userDetails.getGoal());

        calculateDailyCalorieGoal(user);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }


    private void calculateDailyCalorieGoal(User user) {
        double bmr;
        bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() + (user.getId() % 2 == 0 ? -161 : 5);

        double activityFactor;
        switch (user.getGoal()) {
            case WEIGHT_LOSS:
                activityFactor = 1.2; // Sedentary
                break;
            case MAINTENANCE:
                activityFactor = 1.55; // Moderately active
                break;
            case WEIGHT_GAIN:
                activityFactor = 1.75; // Very active
                break;
            default:
                activityFactor = 1.4;
        }

        double dailyCalorieGoal = bmr * activityFactor;

        switch (user.getGoal()) {
            case WEIGHT_LOSS:
                dailyCalorieGoal -= 500;
                break;
            case WEIGHT_GAIN:
                dailyCalorieGoal += 500;
                break;
        }

        user.setDailyCalorieGoal(dailyCalorieGoal);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();

    }
}

