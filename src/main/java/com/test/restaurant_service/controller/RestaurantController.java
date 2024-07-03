package com.test.restaurant_service.controller;

import com.test.restaurant_service.model.Restaurant;
import com.test.restaurant_service.model.TableAvailability;
import com.test.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.addRestaurant(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurantDetails) {
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, restaurantDetails);
        return ResponseEntity.ok(updatedRestaurant);
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants(page, size);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    @PostMapping("/{id}/availability")
    public ResponseEntity<TableAvailability> manageTableAvailability(@PathVariable Long id, @RequestBody TableAvailability availability) {
        TableAvailability tableAvailability = restaurantService.manageTableAvailability(id, availability);
        return ResponseEntity.status(HttpStatus.CREATED).body(tableAvailability);
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<TableAvailability> getTableAvailability(@PathVariable Long id, @RequestParam String date) {
        TableAvailability availability = restaurantService.getTableAvailability(id, date);
        return ResponseEntity.ok(availability);
    }
}
