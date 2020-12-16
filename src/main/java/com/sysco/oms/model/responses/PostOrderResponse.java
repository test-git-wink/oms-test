package com.sysco.oms.model.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Post order response.
 */
@AllArgsConstructor
@Data
public class PostOrderResponse {
    private Long orderId;
    private String orderStatus;
    private String message;

    /**
     * Instantiates a new Post order response.
     *
     * @param orderStatus the order status
     * @param message     the message
     */
    public PostOrderResponse(String orderStatus, String message) {
        this.orderStatus = orderStatus;
        this.message = message;
    }
}
