package com.example.demo.service;

import com.example.demo.dto.OrderItemReqDto;
import com.example.demo.dto.OrderReqDto;
import com.example.demo.dto.OrderRespDto;
import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final OrderMapper mapper;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, RestaurantRepository restaurantRepository, MenuRepository menuRepository, OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OrderRespDto placeOrder(OrderReqDto request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found: " + request.getRestaurantId()));

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemReqDto line : request.getItems()) {
            Menu menu = menuRepository
                    .findByIdAndRestaurantId(line.getMenuId(), restaurant.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Menu item not found in this restaurant: " + line.getMenuId()));

            if (menu.getStock() < line.getQuantity()) {
                throw new BadRequestException("Not enough stock for " + menu.getName());
            }
            menu.setStock(menu.getStock() - line.getQuantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMenu(menu);
            item.setQuantity(line.getQuantity());
            item.setPriceAtOrder(menu.getPrice());
            order.getItems().add(item);

            total = total.add(menu.getPrice()
                    .multiply(BigDecimal.valueOf(line.getQuantity())));
        }

        order.setTotal(total);
        return mapper.mapToDto(orderRepository.save(order));
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true )
    public List<OrderRespDto> getMyOrders(String email) {
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email).stream()
                .map(mapper::mapToDto)
                .toList();
    }
}
