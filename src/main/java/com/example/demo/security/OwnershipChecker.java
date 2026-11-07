package com.example.demo.security;

import com.example.demo.entity.Restaurant;
import com.example.demo.exception.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class OwnershipChecker {

    public void check(Restaurant restaurant, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = restaurant.getOwner() != null
                && restaurant.getOwner().getEmail().equals(auth.getName());
        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("You can only change your own restaurant");
        }
    }
}