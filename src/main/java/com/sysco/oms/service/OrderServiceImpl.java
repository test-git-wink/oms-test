package com.sysco.oms.service;


import com.sysco.oms.constants.StatusConst.OrderStatus;
import com.sysco.oms.model.OrderData;
import com.sysco.oms.model.OrderItem;
import com.sysco.oms.model.ProdQuantity;
import com.sysco.oms.model.requests.OrderRequest;
import com.sysco.oms.model.requests.OrderUpdateRequest;
import com.sysco.oms.repository.*;
import com.sysco.oms.util.CommonUtilities;
import com.sysco.oms.util.OrderUtilites;
import com.sysco.oms.validation.OrderValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * The type Order service.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderValidation orderValidation;
    @Autowired
    private OrderUtilites orderUtilites;
    @Autowired
    private ProductsRepo productsRepo;
    @Autowired
    private DeliveryRepo deliveryRepo;
    @Autowired
    private DeliveryCustomRepo deliveryCustomRepo;
    @Autowired
    private OrderCustomRepo orderCustomRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;

    @Transactional
    @Override
    public Long placeOrder(OrderRequest orderRequest) {

        double orderTotalPrice = 0;
        List<OrderItem> orderItemsPersist = new ArrayList<>();
        List<ProdQuantity> getOrderingProducts = orderValidation.validOrderItemList(orderRequest.getOrderItemList());

        for (ProdQuantity prodQuantity : getOrderingProducts) {
            double productPrice = productsRepo.findProductPriceById(prodQuantity.getProductId()) * prodQuantity.getQuantity();
            orderTotalPrice += productPrice;
            orderItemsPersist.add(new OrderItem(prodQuantity.getQuantity(), productPrice, prodQuantity.getProductId()));
        }

        Integer deliveryId = addDelivery(orderUtilites.getDeliveryDate(), orderRequest.getUserAddresID(), "pending");

        Long orderId = orderCustomRepo.insertOrder(orderRequest, deliveryId, CommonUtilities.getInvoiceId()
                , CommonUtilities.getCurrentTimeStamp(), orderTotalPrice);

        for (OrderItem orderItem : orderItemsPersist) {
            orderItem.setOrderId(orderId);
        }
        orderItemRepo.saveAll(orderItemsPersist);

        for (ProdQuantity prodQuantity : getOrderingProducts) {
            productsRepo.updateProductQuantity(prodQuantity.getProductId(), prodQuantity.getQuantity());
        }

        LOGGER.info("OrderServiceImpl placeOrder() in:{ params: orderRequest={} } out:{ orderId={} } ", orderRequest, orderId);
        return orderId;
    }

    @Override
    public List<OrderData> getOrders(String fromDate, String toDate, String page, String limit) {

        LOGGER.info("OrderServiceImpl getOrders() {params: fromDate={},toDate={}", fromDate, toDate);

        Map<String, Date> orderSearchDate = orderUtilites.getOrderSearchDateRangeDate(fromDate, toDate);

        Pageable pageable = PageRequest.of(Integer.parseInt(page),Integer.parseInt(limit));

        return
                orderRepo.findOrderDataByDateRange(orderSearchDate.get("fromDate")
                        , orderSearchDate.get("toDate"), pageable).getContent().stream().map(it -> new OrderData(it.getOrderId()
                        , it.getUser().getUserId(), it.getOrderTotalPrice(), it.getOrderTimestamp(), it.getStatus().toString(),
                        it.getDelivery().getDeliveryDate()
                        , it.getDelivery().getDeliveryStatus()
                        , it.getDelivery().getUserAddress().getConcatAddress())).collect(Collectors.toList());

    }

    @Override
    public Integer cancelOrder(Long orderId, OrderUpdateRequest orderUpdateRequest) {

        LOGGER.info("OrderServiceImpl cancelOrder() {params: orderId={},orderUpdateRequest={}", orderId, orderUpdateRequest);

        if (orderValidation.isValidOrderCancelRequest(orderId,orderUpdateRequest)) {

            return orderRepo.updateOrderStatus(orderId, OrderStatus.cancel.toString());

        } else
            return 0;
    }

    @Override
    public Integer addDelivery(LocalDate deliveryDate, Integer userAddressId, String deliveryStatus) {

        return deliveryCustomRepo.insertDelivery(deliveryDate, userAddressId, deliveryStatus);
    }
}
