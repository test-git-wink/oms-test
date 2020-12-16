package com.sysco.oms.repository;

import com.sysco.oms.model.requests.OrderRequest;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderCustomRepo {

    Long insertOrder(OrderRequest orderRequest, Integer deliveryId, String invoiceId, Date orderTimeStamp,Double orderTotalPrice);
}
