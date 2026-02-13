package com.project.sapaadu.repository;

import com.project.sapaadu.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndMenuItemId(Long cartId, Long menuItemId);
}