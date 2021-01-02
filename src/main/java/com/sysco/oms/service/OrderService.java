package com.sysco.oms.service;

import com.sysco.oms.model.OrderData;
import com.sysco.oms.model.requests.OrderRequest;
import com.sysco.oms.model.requests.OrderUpdateRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * The interface Order service.
 */
public interface OrderService {

    /**
     * Place order long.
     *
     * @param orderRequest the order request
     * @return the long
     */
    Long placeOrder(OrderRequest orderRequest);

    /**
     * Gets orders.
     *
     * @param fromDate the from date
     * @param toDate   the to date
     * @param page     the page
     * @param limit    the limit
     * @return the orders
     */
    List<OrderData> getOrders(String fromDate, String toDate, String page,String limit);

    /**
     * Cancel order integer.
     *
     * @param orderId            the order id
     * @param orderUpdateRequest the order update request
     * @return the integer
     */
    Integer cancelOrder(Long orderId, OrderUpdateRequest orderUpdateRequest);

    /**
     * Add delivery integer.
     *
     * @param deliveryDate   the delivery date
     * @param userAddressId  the user address id
     * @param deliveryStatus the delivery status
     * @return the integer
     */
    Integer addDelivery(LocalDate deliveryDate, Integer userAddressId, String deliveryStatus);
}
