package com.project.sapaadu.repository;

import com.project.sapaadu.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
