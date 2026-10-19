package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class RestaurantReqDto {
    @NotBlank(message = "name cant be null")
    private String name;
    @NotBlank(message = "city is required")
    private String city;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
