package com.sysco.oms.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Prod quantity.
 */
@AllArgsConstructor
@Data
public class ProdQuantity {
    private String productId;
    private float quantity;
}
