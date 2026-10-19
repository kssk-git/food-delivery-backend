package com.example.demo.mapper;

import com.example.demo.dto.RestaurantReqDto;
import com.example.demo.dto.RestaurantRespDto;
import com.example.demo.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {
    public Restaurant mapToEntity(RestaurantReqDto request){
       Restaurant restaurant = new Restaurant();
       restaurant.setName(request.getName());
       restaurant.setCity(request.getCity());
       return restaurant;
    }
    public void updateEntity(Restaurant existing, RestaurantReqDto request) {
        existing.setName(request.getName());
        existing.setCity(request.getCity());
    }


    public RestaurantRespDto mapToDto(Restaurant restaurant){
        RestaurantRespDto response = new RestaurantRespDto();
        response.setCity(restaurant.getCity());
        response.setName(restaurant.getName());
        response.setId(restaurant.getId());
        return response;
    }
}
