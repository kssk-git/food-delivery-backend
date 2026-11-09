package com.example.demo.controller;

import com.example.demo.dto.OrderReqDto;
import com.example.demo.dto.OrderRespDto;
import com.example.demo.dto.StatusUpdateReqDto;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderRespDto place(@Valid @RequestBody OrderReqDto request,
                              Authentication authentication) {
        return orderService.placeOrder(request, authentication.getName());
    }

    @GetMapping("/my")
    public List<OrderRespDto> myOrders(Authentication authentication) {
        return orderService.getMyOrders(authentication.getName());
    }

    @PatchMapping("/{id}/status")
    public OrderRespDto updateStatus(@PathVariable Long id,
                                     @Valid @RequestBody StatusUpdateReqDto request,
                                     Authentication authentication) {
        return orderService.updateStatus(id, request.getStatus(), authentication);
    }

    @PatchMapping("/{id}/cancel")
    public OrderRespDto cancel(@PathVariable Long id, Authentication authentication) {
        return orderService.cancelOrder(id, authentication.getName());
    }
}
