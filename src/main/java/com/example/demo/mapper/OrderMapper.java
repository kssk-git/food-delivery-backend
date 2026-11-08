package com.example.demo.mapper;


import com.example.demo.dto.OrderItemRespDto;
import com.example.demo.dto.OrderRespDto;
import com.example.demo.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderRespDto mapToDto(Order order) {
        OrderRespDto response = new OrderRespDto();
        response.setId(order.getId());
        response.setRestaurantId(order.getRestaurant().getId());
        response.setStatus(order.getStatus());
        response.setTotal(order.getTotal());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(order.getItems().stream().map(item -> {
            OrderItemRespDto line = new OrderItemRespDto();
            line.setMenuId(item.getMenu().getId());
            line.setName(item.getMenu().getName());
            line.setQuantity(item.getQuantity());
            line.setPriceAtOrder(item.getPriceAtOrder());
            return line;
        }).toList());
        return response;
    }
}
