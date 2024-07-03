package com.test.restaurant_service.service;

import com.test.restaurant_service.model.Restaurant;
import com.test.restaurant_service.model.TableAvailability;
import com.test.restaurant_service.repository.RestaurantRepository;
import com.test.restaurant_service.repository.TableAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TableAvailabilityRepository tableAvailabilityRepository;

    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(Long id, Restaurant restaurantDetails) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceAccessException("Restaurant not found"));

        restaurant.setName(restaurantDetails.getName());
        restaurant.setAddress(restaurantDetails.getAddress());
        restaurant.setTotalTables(restaurant.getTotalTables());
        restaurant.setAvailableTables(restaurant.getAvailableTables());

        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findAll(pageable).getContent();
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceAccessException("Restaurant not found"));
    }

    public TableAvailability manageTableAvailability(Long restaurantId, TableAvailability availability) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceAccessException("Restaurant not found"));
        availability.setRestaurant(restaurant);
        return tableAvailabilityRepository.save(availability);
    }

    public TableAvailability getTableAvailability(Long restaurantId, String date) {
        return tableAvailabilityRepository.findByRestaurantIdAndDate(restaurantId, date)
                .orElseThrow(() -> new ResourceAccessException("Availability not found for this date"));
    }
}
