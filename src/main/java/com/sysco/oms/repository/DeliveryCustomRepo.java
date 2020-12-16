package com.sysco.oms.repository;

import java.time.LocalDate;

public interface DeliveryCustomRepo {
    Integer insertDelivery(LocalDate deliveryDate, Integer userAddressId,String deliveryStatus);
}
