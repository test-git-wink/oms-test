package com.sysco.oms.model.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Order update request.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderUpdateRequest {
    private String orderStatus;
}
