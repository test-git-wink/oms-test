package com.sysco.oms.service;

import com.sysco.oms.model.OrderData;
import com.sysco.oms.model.requests.OrderRequest;
import com.sysco.oms.model.requests.OrderUpdateRequest;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    Long placeOrder(OrderRequest orderRequest);

    List<OrderData> getOrders(String fromDate, String toDate, Integer page,Integer limit);

    Integer cancelOrder(Long orderId, OrderUpdateRequest orderUpdateRequest);

    Integer addDelivery(LocalDate deliveryDate, Integer userAddressId, String deliveryStatus);
}
