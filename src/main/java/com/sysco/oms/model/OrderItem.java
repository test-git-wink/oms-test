package com.sysco.oms.model;

import lombok.Data;

import javax.persistence.*;


/**
 * The type Order item.
 */
@Data
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long orderItemId;
    @Column(name = "quantity")
    private Float quantity;
    @Column(name = "order_product_total_price")
    private Double orderItemprice;
    @Column(name = "product_id")
    private String productId;
    @Column(name = "order_id")
    private Long orderId;

    /**
     * Instantiates a new Order item.
     *
     * @param quantity       the quantity
     * @param orderItemprice the order itemprice
     * @param productId      the product id
     */
    public OrderItem(Float quantity, Double orderItemprice, String productId) {
        this.quantity = quantity;
        this.orderItemprice = orderItemprice;
        this.productId = productId;
    }
}
