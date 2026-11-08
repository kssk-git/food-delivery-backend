package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderReqDto {
    @NotNull(message = "restaurantId is required")
    private Long restaurantId;

    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemReqDto> items;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<OrderItemReqDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemReqDto> items) {
        this.items = items;
    }
}
