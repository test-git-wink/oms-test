package com.sysco.oms.repository;

import com.sysco.oms.model.Delivery;
import com.sysco.oms.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

    @Query(countQuery = "SELECT count(order_id) from order where order_id = ?1",nativeQuery = true)
    Long findValidityByOrderId(Long userId);

    Long countByOrderId(Long orderId);

    @Query(value = "SELECT o" +
            " FROM Order o "+
            " where o.orderTimestamp >=?1 and o.orderTimestamp < ?2 ")
    Page<Order> findOrderDataByDateRange(Date fromDate, Date toDate, Pageable page);

    @Query(value = "SELECT count(o)" +
            " FROM Order o "+
            " where o.orderTimestamp >=?1 and o.orderTimestamp < ?2 ")
    Long countOrderDataByDateRange(Date fromDate, Date toDate);

    @Transactional
    @Modifying
    @Query(value = "Update orders  set order_status = ?2 where order_id = ?1 ",nativeQuery = true)
    int updateOrderStatus(Long orderId, String orderStatus);

    @Query(value = "SELECT o.status FROM Order o WHERE o.orderId = ?1")
    String findOrderStatusById(Long orderId);

    @Query(value = "SELECT o.delivery FROM Order o WHERE o.orderId = ?1")
    Delivery findOrderDeliveryById(Long orderId);

}


