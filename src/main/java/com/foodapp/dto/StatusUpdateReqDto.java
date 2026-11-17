package com.foodapp.dto;

import com.foodapp.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateReqDto {
    @NotNull(message = "status is required")
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}