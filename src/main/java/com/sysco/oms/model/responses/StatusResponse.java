package com.sysco.oms.model.responses;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * The type Status response.
 */
@AllArgsConstructor
@Data
public class StatusResponse {
    private String status;
    private String message;

    /**
     * Instantiates a new Status response.
     *
     * @param status the status
     */
    public StatusResponse(String status) {
        this.status = status;
    }

}
