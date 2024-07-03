package com.test.restaurant_service.service;

import com.test.restaurant_service.model.Restaurant;
import com.test.restaurant_service.model.TableAvailability;
import com.test.restaurant_service.repository.RestaurantRepository;
import static org.junit.jupiter.api.Assertions.*;

import com.test.restaurant_service.repository.TableAvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringBootTest
public class RestaurantServiceTest {
    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private TableAvailabilityRepository tableAvailabilityRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddRestaurant() {
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        Restaurant result = restaurantService.addRestaurant(restaurant);

        assertNotNull(result);
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    public void testUpdateRestaurant() {
        Long id = 1L;
        Restaurant existingRestaurant = new Restaurant();
        existingRestaurant.setId(id);
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(existingRestaurant);

        Restaurant updatedDetails = new Restaurant();
        updatedDetails.setName("Updated Name");

        Restaurant result = restaurantService.updateRestaurant(id, updatedDetails);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(restaurantRepository, times(1)).findById(id);
        verify(restaurantRepository, times(1)).save(existingRestaurant);
    }

    @Test
    public void testGetAllRestaurants() {
        int page = 0;
        int size = 10;
        List<Restaurant> restaurantList = Arrays.asList(new Restaurant(), new Restaurant());
        Page<Restaurant> restaurantPage = new PageImpl<>(restaurantList);
        Pageable pageable = PageRequest.of(page, size);

        when(restaurantRepository.findAll(pageable)).thenReturn(restaurantPage);

        List<Restaurant> result = restaurantService.getAllRestaurants(page, size);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restaurantRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetRestaurantById() {
        Long id = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.getRestaurantById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(restaurantRepository, times(1)).findById(id);
    }

    @Test
    public void testManageTableAvailability() {
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        TableAvailability availability = new TableAvailability();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(tableAvailabilityRepository.save(any(TableAvailability.class))).thenReturn(availability);

        TableAvailability result = restaurantService.manageTableAvailability(restaurantId, availability);

        assertNotNull(result);
        assertEquals(restaurant, result.getRestaurant());
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(tableAvailabilityRepository, times(1)).save(availability);
    }

    @Test
    public void testGetTableAvailability() {
        Long restaurantId = 1L;
        String date = "2024-06-28";
        TableAvailability availability = new TableAvailability();
        when(tableAvailabilityRepository.findByRestaurantIdAndDate(restaurantId, date)).thenReturn(Optional.of(availability));

        TableAvailability result = restaurantService.getTableAvailability(restaurantId, date);

        assertNotNull(result);
        verify(tableAvailabilityRepository, times(1)).findByRestaurantIdAndDate(restaurantId, date);
    }
}
