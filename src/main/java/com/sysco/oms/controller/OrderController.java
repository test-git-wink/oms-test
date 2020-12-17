package com.sysco.oms.controller;

import com.sysco.oms.model.OrderData;
import com.sysco.oms.model.requests.OrderRequest;
import com.sysco.oms.model.requests.OrderUpdateRequest;
import com.sysco.oms.model.responses.GetOrdersResponse;
import com.sysco.oms.model.responses.PostOrderResponse;
import com.sysco.oms.model.responses.StatusResponse;
import com.sysco.oms.service.OrderService;
import com.sysco.oms.constants.StatusConst;
import com.sysco.oms.validation.CommonValidation;
import com.sysco.oms.validation.OrderValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sysco.oms.constants.CommonConst.INTERNAL_SERVER_ERROR;
import static com.sysco.oms.constants.ResponseStatus.*;


/**
 * The type Order controller.
 */
@CrossOrigin(origins = "http://localhost:8081/v1")
@RestController
@RequestMapping(value = "/v1/customer-orders/order")
class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderValidation orderValidation;

    @Autowired
    private CommonValidation commonValidation;

    /**
     * Create order.
     *
     * @param orderRequest the order request
     */
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<PostOrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        Long orderId;
        try {
            LOGGER.info("OrderController createOrder() parameters : [OrderRequest={} ]", orderRequest);

            if (orderValidation.isValidOrderRequest(orderRequest)) {

                orderId = orderService.placeOrder(orderRequest);

                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new PostOrderResponse(orderId, StatusConst.OrderStatus.placed.toString(),"Order placed"));

            }
            else

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new PostOrderResponse(StatusConst.OrderStatus.fail.toString(), "Invalid order request"));

        } catch (Exception e) {
            LOGGER.error("OrderController createOrder() responseStatus: {} ", SERVER_ERROR);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PostOrderResponse(StatusConst.OrderStatus.fail.toString(), INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * Gets orders.
     *
     * @param fromDate the from date
     * @param toDate   the to date
     * @param page     the page
     * @param pageLimit    the limit
     * @return the orders
     */
    @GetMapping(value = "/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetOrdersResponse> getOrders(
            @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate,
            @RequestParam(value = "page") String page
            , @RequestParam(value = "limit") String pageLimit) {

        try {
            LOGGER.info("OrderController getOrders() parameters [fromDate:{} ,toDate: {} ,page: {}]", fromDate, toDate, page);

            if (commonValidation.isVallidDateRange(fromDate, toDate) && commonValidation.isValidPositiveNumber(page)
                    && commonValidation.isValidPositiveNumber(pageLimit)) {

                List<OrderData> orders = orderService.getOrders(fromDate, toDate
                        , Integer.parseInt(page), Integer.parseInt(pageLimit));
                return ResponseEntity.status(HttpStatus.OK).body(new GetOrdersResponse(orders, SUCCESS, "Successful"));

            } else {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new GetOrdersResponse(INVALID_INPUTS, "Invalid date range or page number"));
            }
        } catch (Exception e) {
            LOGGER.error("OrderController getOrders() responseStatus: {} ", SERVER_ERROR);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GetOrdersResponse(SERVER_ERROR, INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * Update order response entity.
     *
     * @param orderId            the order id
     * @param orderUpdateRequest the order update request
     * @return the response entity
     */
    @PatchMapping(value = "/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusResponse> updateOrder(@PathVariable(value = "orderId", required = true) String orderId
            , @RequestBody OrderUpdateRequest orderUpdateRequest) {
        try {
            LOGGER.info("OrderController updateOrder() parameters [orderId:{} ,orderUpdateRequest: {} ]", orderId, orderUpdateRequest);
            if (commonValidation.isValidPositiveNumber(orderId) && orderValidation.isValidOrder(Long.parseLong(orderId))) {
                orderService.cancelOrder(Long.parseLong(orderId), orderUpdateRequest);
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new StatusResponse(SUCCESS, "Successful"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new StatusResponse(INVALID_INPUTS, "Invalid orderId or status"));
            }

        } catch (Exception e) {
            LOGGER.error("OrderController updateOrder() responseStatus: {} error {}", SERVER_ERROR,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StatusResponse(SERVER_ERROR, INTERNAL_SERVER_ERROR));
        }

    }
}
