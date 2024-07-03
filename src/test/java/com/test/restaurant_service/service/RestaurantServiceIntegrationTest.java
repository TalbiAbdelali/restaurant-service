package com.test.restaurant_service.service;

import com.test.restaurant_service.model.Restaurant;
import com.test.restaurant_service.repository.RestaurantRepository;
import com.test.restaurant_service.model.TableAvailability;
import static org.junit.jupiter.api.Assertions.*;

import com.test.restaurant_service.repository.TableAvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestaurantServiceIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TableAvailabilityRepository tableAvailabilityRepository;

    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        restaurantRepository.deleteAll();
        tableAvailabilityRepository.deleteAll();
        restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test St");
        restaurant.setTotalTables(10);
        restaurant.setAvailableTables(5);
        restaurant = restaurantRepository.save(restaurant);
    }

    @Test
    public void testAddRestaurant() {
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName("New Restaurant");
        newRestaurant.setAddress("456 New St");
        newRestaurant.setTotalTables(20);
        newRestaurant.setAvailableTables(15);

        ResponseEntity<Restaurant> response = restTemplate.postForEntity("/api/restaurant", newRestaurant, Restaurant.class);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("New Restaurant", response.getBody().getName());
    }

    @Test
    public void testUpdateRestaurant() {
        Restaurant updatedDetails = new Restaurant();
        updatedDetails.setName("Updated Name");
        updatedDetails.setAddress("Updated Address");
        updatedDetails.setTotalTables(20);
        updatedDetails.setAvailableTables(10);

        HttpEntity<Restaurant> requestUpdate = new HttpEntity<>(updatedDetails);
        ResponseEntity<Restaurant> response = restTemplate.exchange("/api/restaurant/" + restaurant.getId(), HttpMethod.PUT, requestUpdate, Restaurant.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    public void testGetAllRestaurants() {
        ResponseEntity<Restaurant[]> response = restTemplate.getForEntity("/api/restaurant?page=0&size=10", Restaurant[].class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    public void testGetRestaurantById() {
        ResponseEntity<Restaurant> response = restTemplate.getForEntity("/api/restaurant/" + restaurant.getId(), Restaurant.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(restaurant.getId(), response.getBody().getId());
    }

    @Test
    public void testManageTableAvailability() {
        TableAvailability availability = new TableAvailability();
        availability.setDate("2024-06-28");
        availability.setAvailableTables(8);

        ResponseEntity<TableAvailability> response = restTemplate.postForEntity("/api/restaurant/" + restaurant.getId() + "/availability", availability, TableAvailability.class);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("2024-06-28", response.getBody().getDate());
    }

    @Test
    public void testGetTableAvailability() {
        TableAvailability availability = new TableAvailability();
        availability.setDate("2024-06-28");
        availability.setAvailableTables(8);
        availability.setRestaurant(restaurant);
        tableAvailabilityRepository.save(availability);

        ResponseEntity<TableAvailability> response = restTemplate.getForEntity("/api/restaurant/" + restaurant.getId() + "/availability?date=2024-06-28", TableAvailability.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("2024-06-28", response.getBody().getDate());
    }
}
