package com.foodapp.service;

import com.foodapp.dto.OrderItemReqDto;
import com.foodapp.dto.OrderReqDto;
import com.foodapp.dto.OrderRespDto;
import com.foodapp.entity.*;
import com.foodapp.entity.*;
import com.foodapp.exception.BadRequestException;
import com.foodapp.exception.ForbiddenException;
import com.foodapp.mapper.OrderMapper;
import com.foodapp.repository.MenuRepository;
import com.foodapp.repository.OrderRepository;
import com.foodapp.repository.RestaurantRepository;
import com.foodapp.repository.UserRepository;
import com.foodapp.security.OwnershipChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    MenuRepository menuRepository;
    @Mock
    OwnershipChecker ownershipChecker;
    @Spy
    OrderMapper mapper = new OrderMapper();

    @InjectMocks
    OrderService orderService;

    private User user(String email) {
        User u = new User();
        u.setEmail(email);
        return u;
    }

    private Restaurant restaurant() {
        Restaurant r = new Restaurant();
        r.setId(1L);
        return r;
    }

    private Menu menu(int stock) {
        Menu m = new Menu();
        m.setId(1L);
        m.setName("Pizza");
        m.setPrice(BigDecimal.valueOf(299));
        m.setStock(stock);
        return m;
    }

    private OrderReqDto request(int quantity) {
        OrderItemReqDto line = new OrderItemReqDto();
        line.setMenuId(1L);
        line.setQuantity(quantity);
        OrderReqDto req = new OrderReqDto();
        req.setRestaurantId(1L);
        req.setItems(List.of(line));
        return req;
    }

    @Test
    void placeOrder_reducesStockAndCalculatesTotal() {
        Menu menu = menu(20);
        when(userRepository.findByEmail("c@test.com")).thenReturn(Optional.of(user("c@test.com")));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(menuRepository.findByIdAndRestaurantId(1L, 1L)).thenReturn(Optional.of(menu));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderRespDto result = orderService.placeOrder(request(2), "c@test.com");

        assertEquals(0, result.getTotal().compareTo(BigDecimal.valueOf(598)));
        assertEquals(OrderStatus.PLACED, result.getStatus());
        assertEquals(18, menu.getStock());
    }

    @Test
    void placeOrder_notEnoughStock_throwsAndSavesNothing() {
        Menu menu = menu(1);
        when(userRepository.findByEmail("c@test.com")).thenReturn(Optional.of(user("c@test.com")));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(menuRepository.findByIdAndRestaurantId(1L, 1L)).thenReturn(Optional.of(menu));

        assertThrows(BadRequestException.class,
                () -> orderService.placeOrder(request(5), "c@test.com"));

        assertEquals(1, menu.getStock());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void cancelOrder_byAnotherUser_isForbidden() {
        Order order = new Order();
        order.setUser(user("a@test.com"));
        order.setStatus(OrderStatus.PLACED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ForbiddenException.class,
                () -> orderService.cancelOrder(1L, "b@test.com"));
    }

    @Test
    void cancelOrder_restoresStock() {
        Menu menu = menu(18);
        OrderItem item = new OrderItem();
        item.setMenu(menu);
        item.setQuantity(2);

        Order order = new Order();
        order.setUser(user("a@test.com"));
        order.setRestaurant(restaurant());
        order.setStatus(OrderStatus.PLACED);
        order.setTotal(BigDecimal.valueOf(598));
        order.getItems().add(item);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        orderService.cancelOrder(1L, "a@test.com");

        assertEquals(20, menu.getStock());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }
}