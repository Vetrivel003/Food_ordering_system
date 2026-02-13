package com.project.sapaadu.repository;


import com.project.sapaadu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);
}
