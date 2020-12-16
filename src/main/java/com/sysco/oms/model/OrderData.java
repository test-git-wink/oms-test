package com.sysco.oms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;


/**
 * The type Order data.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class OrderData {


    private Long orderId;

    private Long customerId;

    private Double orderTotalPrice;

    private Date orderTimestamp;

    private String orderStatus;

    private LocalDate deliveryDate;

    private String deliveryStatus;

    private String deliveryAddress;

}
