package com.example.demo.controller;

import com.example.demo.dto.RestaurantReqDto;
import com.example.demo.dto.RestaurantRespDto;
import com.example.demo.entity.Restaurant;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public RestaurantRespDto add(@Valid @RequestBody RestaurantReqDto request, Authentication authentication){
      return  restaurantService.createRestaurant(request,authentication.getName());
    }

    @GetMapping("/{id}")
    public RestaurantRespDto getone(@PathVariable Long id){
        return restaurantService.getRestaurant(id);
    }

    @GetMapping
    public List<RestaurantRespDto> findall(){
        return restaurantService.getall();
    }

    @PutMapping("/{id}")
    public RestaurantRespDto update(@Valid @RequestBody RestaurantReqDto request,
                             @PathVariable Long id,Authentication authentication){
       return restaurantService.update(request,id,authentication);

    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id,Authentication authentication){
        restaurantService.deleting(id,authentication);
        return "done";
    }
}
