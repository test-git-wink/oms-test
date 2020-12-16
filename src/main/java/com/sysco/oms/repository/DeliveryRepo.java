package com.sysco.oms.repository;

import com.sysco.oms.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepo extends JpaRepository<Delivery,Integer> {
}
