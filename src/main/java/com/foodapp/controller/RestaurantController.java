package com.foodapp.controller;

import com.foodapp.dto.PageRespDto;
import com.foodapp.dto.RestaurantReqDto;
import com.foodapp.dto.RestaurantRespDto;
import com.foodapp.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public PageRespDto<RestaurantRespDto> getAll(
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return restaurantService.getAll(city, page, size, sortBy);
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
