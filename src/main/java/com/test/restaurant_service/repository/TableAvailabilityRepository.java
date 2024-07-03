package com.test.restaurant_service.repository;

import com.test.restaurant_service.model.TableAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableAvailabilityRepository extends JpaRepository<TableAvailability, Long> {
    Optional<TableAvailability> findByRestaurantIdAndDate(Long restaurantId, String date);
}
