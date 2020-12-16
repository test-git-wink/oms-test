package com.sysco.oms.model;

import com.sysco.oms.constants.StatusConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;


/**
 * The type Order.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "order_total_price")
    private Double orderTotalPrice;
    @Column(name = "order_timestamp")
    private Date orderTimestamp;
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private StatusConst.OrderStatus status;
    @Column(name = "invoice_id")
    private String invoiceId;
    @Column(name = "delivery_id")
    private Integer deliveryId;
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private Delivery delivery;

    /**
     * Instantiates a new Order.
     *
     * @param orderTotalPrice the order total price
     * @param orderTimestamp  the order timestamp
     * @param status          the status
     * @param invoiceId       the invoice id
     * @param deliveryId      the delivery id
     * @param userId          the user id
     */
    public Order(Double orderTotalPrice, Date orderTimestamp, StatusConst.OrderStatus status, String invoiceId, Integer deliveryId, Long userId) {
        this.orderTotalPrice = orderTotalPrice;
        this.orderTimestamp = orderTimestamp;
        this.status = status;
        this.invoiceId = invoiceId;
        this.deliveryId = deliveryId;
        this.userId = userId;
    }

    /**
     * Instantiates a new Order.
     *
     * @param orderId         the order id
     * @param orderTotalPrice the order total price
     * @param orderTimestamp  the order timestamp
     * @param status          the status
     * @param invoiceId       the invoice id
     * @param deliveryId      the delivery id
     * @param userId          the user id
     */
    public Order(Long orderId, Double orderTotalPrice, Date orderTimestamp, StatusConst.OrderStatus status, String invoiceId, Integer deliveryId, Long userId) {
        this.orderId = orderId;
        this.orderTotalPrice = orderTotalPrice;
        this.orderTimestamp = orderTimestamp;
        this.status = status;
        this.invoiceId = invoiceId;
        this.deliveryId = deliveryId;
        this.userId = userId;
    }
}
