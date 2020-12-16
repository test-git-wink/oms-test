package com.sysco.oms.model.responses;

import com.sysco.oms.model.OrderData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * The type Get orders response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class GetOrdersResponse {

    private List<OrderData> orders;

    private String status;

    private String message;

    /**
     * Instantiates a new Get orders response.
     *
     * @param status the status
     */
    public GetOrdersResponse(String status) {
        this.status = status;
    }

    /**
     * Instantiates a new Get orders response.
     *
     * @param status  the status
     * @param message the message
     */
    public GetOrdersResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
