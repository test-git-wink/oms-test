package com.sysco.oms.model.requests;

import com.sysco.oms.model.ProdQuantity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


/**
 * The type Order request.
 */
@AllArgsConstructor
@Data
public class OrderRequest {
    private Long userId;
    private List<ProdQuantity> orderItemList;
    private String orderStatus;
    private String shipmentDate;
    private Integer userAddresID;
}
