package com.sysco.oms.repository;

import com.sysco.oms.model.Delivery;
import com.sysco.oms.util.OrderUtilites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

@Service

public class DeliveryCustomRepoImpl implements DeliveryCustomRepo{

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private OrderUtilites orderUtilites;


    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    @Override
    public Integer insertDelivery(LocalDate deliveryDate, Integer userAddressId,String deliveryStatus) {

        Delivery delivery=new Delivery(deliveryDate,deliveryStatus,userAddressId);
        entityManager.persist(delivery);
        entityManager.flush();

        return delivery.getDeliveryId();
    }
}
