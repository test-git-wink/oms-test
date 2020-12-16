package com.sysco.oms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The type Product.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id")
    private String productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_type")
    private String productType;
    @Column(name = "sell_price")
    private Double productSellPrice;
    @Column(name = "batch_price")
    private Double productBatchPrice;
    @Column(name = "in_stock_quantity")
    private float inStockQuantiy;
    @Column(name = "product_measure_unit")
    private String prodMeasureUnit;


}
