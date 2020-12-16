package com.sysco.oms.repository;

import com.sysco.oms.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {
}
