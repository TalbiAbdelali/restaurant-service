package com.test.restaurant_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.Arrays;
import java.util.List;

import com.test.restaurant_service.model.Restaurant;
import com.test.restaurant_service.model.TableAvailability;
import com.test.restaurant_service.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(restaurantController).build();
    }

    @Test
    public void testAddRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");

        when(restaurantService.addRestaurant(any(Restaurant.class))).thenReturn(restaurant);

        mockMvc.perform(post("/api/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(restaurant)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Restaurant"));

        verify(restaurantService, times(1)).addRestaurant(any(Restaurant.class));
    }

    @Test
    public void testUpdateRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Updated Restaurant");

        when(restaurantService.updateRestaurant(anyLong(), any(Restaurant.class))).thenReturn(restaurant);

        mockMvc.perform(put("/api/restaurant/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(restaurant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Restaurant"));

        verify(restaurantService, times(1)).updateRestaurant(anyLong(), any(Restaurant.class));
    }

    @Test
    public void testGetAllRestaurants() throws Exception {
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Restaurant 1");

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Restaurant 2");

        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2);

        when(restaurantService.getAllRestaurants(anyInt(), anyInt())).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurant")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Restaurant 1"))
                .andExpect(jsonPath("$[1].name").value("Restaurant 2"));

        verify(restaurantService, times(1)).getAllRestaurants(anyInt(), anyInt());
    }

    @Test
    public void testGetRestaurantById() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");

        when(restaurantService.getRestaurantById(anyLong())).thenReturn(restaurant);

        mockMvc.perform(get("/api/restaurant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"));

        verify(restaurantService, times(1)).getRestaurantById(anyLong());
    }

    @Test
    public void testManageTableAvailability() throws Exception {
        TableAvailability availability = new TableAvailability();
        availability.setDate("2024-06-28");

        when(restaurantService.manageTableAvailability(anyLong(), any(TableAvailability.class))).thenReturn(availability);

        mockMvc.perform(post("/api/restaurant/1/availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(availability)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.date").value("2024-06-28"));

        verify(restaurantService, times(1)).manageTableAvailability(anyLong(), any(TableAvailability.class));
    }

    @Test
    public void testGetTableAvailability() throws Exception {
        TableAvailability availability = new TableAvailability();
        availability.setDate("2024-06-28");

        when(restaurantService.getTableAvailability(anyLong(), anyString())).thenReturn(availability);

        mockMvc.perform(get("/api/restaurant/1/availability")
                        .param("date", "2024-06-28"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2024-06-28"));

        verify(restaurantService, times(1)).getTableAvailability(anyLong(), anyString());
    }
}
