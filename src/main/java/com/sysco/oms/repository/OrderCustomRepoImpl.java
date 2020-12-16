package com.sysco.oms.repository;

import com.sysco.oms.model.Order;
import com.sysco.oms.model.requests.OrderRequest;
import com.sysco.oms.util.OrderUtilites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Service
public class OrderCustomRepoImpl implements OrderCustomRepo{

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private OrderUtilites orderUtilites;

    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    @Override
    public Long insertOrder(OrderRequest orderRequest, Integer deliveryId, String invoiceId, Date orderTimeStamp,Double orderTotalPrice) {
        Order order=new Order(orderTotalPrice,orderTimeStamp
                ,orderUtilites.getEnumOrderStatus(orderRequest.getOrderStatus()),invoiceId,deliveryId,orderRequest.getUserId());
        entityManager.persist(order);
        entityManager.flush();
        return order.getOrderId();
    }
}
