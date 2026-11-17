package com.foodapp.repository;

import com.foodapp.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
Optional<Menu> findByIdAndRestaurantId(Long id,Long restaurantId);
    List<Menu> findByRestaurantId(Long restaurantId);
}
