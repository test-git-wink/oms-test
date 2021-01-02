package com.sysco.oms.controller;

import com.sysco.oms.constants.ResponseStatus;
import com.sysco.oms.constants.StatusConst;
import com.sysco.oms.model.OrderData;
import com.sysco.oms.model.ProdQuantity;
import com.sysco.oms.model.requests.OrderRequest;
import com.sysco.oms.model.requests.OrderUpdateRequest;
import com.sysco.oms.service.OrderService;
import com.sysco.oms.util.CommonUtilities;
import com.sysco.oms.validation.CommonValidation;
import com.sysco.oms.validation.OrderValidation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sysco.oms.constants.CommonConst.INTERNAL_SERVER_ERROR;
import static com.sysco.oms.constants.ResponseStatus.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private OrderController orderController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderValidation orderValidation;

    @MockBean
    private CommonValidation commonValidation;


    @Test
    void getOrdersTestSuccess() throws Exception {
        String fromDate1 = "2020-12-01";
        String toDate1 = "2020-12-12";
        String page1 = "1";
        String pageLimit = "10";

        List<OrderData> orderDataList1 = Stream.of(new OrderData(1000L, 10L, 1000.0, new Date(), "approved", LocalDate.of(2020, 12, 14), "pending", "45,Abc Rd,SL")
                , new OrderData(1001L, 11L, 1000.0, new Date(), "approved", LocalDate.of(2020, 12, 14), "pending", "45,Abc Rd,SL")).collect(Collectors.toList());

        Mockito.when(orderService.getOrders(fromDate1, toDate1, page1, pageLimit)).thenReturn(orderDataList1);
        Mockito.when(orderValidation.isValidGetOrderRequest(fromDate1, toDate1,page1,pageLimit)).thenReturn(true);
//        Mockito.when(commonValidation.isValidPositiveNumber(page1)).thenReturn(true);
//        Mockito.when(commonValidation.isValidPositiveNumber(pageLimit)).thenReturn(true);

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        mockMvc.perform(get("/v1/customer-orders/order/")
                .param("fromDate", fromDate1).param("toDate", toDate1).param("page", page1).param("limit", pageLimit))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orders[0].orderId", is(1000)))
                .andExpect(jsonPath("$.orders[0].customerId", is(10)))
                .andExpect(jsonPath("$.orders[0].orderTotalPrice", is(1000.0)))
//                .andExpect( jsonPath("$.orders[0].orderTimestamp",is(formatter.format(new Date()))))
                .andExpect(jsonPath("$.orders[0].orderStatus", is("approved")))
                .andExpect(jsonPath("$.orders[0].deliveryDate", is("2020-12-14")))
                .andExpect(jsonPath("$.orders[0].deliveryStatus", is("pending")))
                .andExpect(jsonPath("$.orders[0].deliveryAddress", is("45,Abc Rd,SL")))
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESS))
                .andExpect(jsonPath("$.message").value("Successful"));
    }

    @Test
    void getOrdersTestBadRequest() throws Exception {
        String fromDate1 = "2020-13-13";
        String toDate1 = "2020-12-34";
        String page1 = "1fg";
        String pageLimit = "1h";

        Mockito.when(orderValidation.isValidGetOrderRequest(fromDate1, toDate1,page1,pageLimit)).thenReturn(false);
//        Mockito.when(commonValidation.isValidPositiveNumber(page1)).thenReturn(false);
//        Mockito.when(commonValidation.isValidPositiveNumber(pageLimit)).thenReturn(false);

        mockMvc.perform(get("/v1/customer-orders/order/")
                .param("fromDate", fromDate1).param("toDate", toDate1).param("page", page1).param("limit", pageLimit))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(INVALID_INPUTS))
                .andExpect(jsonPath("$.message").value("Invalid date range or page number"));
    }

    @Test
    void getOrdersTestBadRequest2() throws Exception {
        String fromDate1 = "2020-13-13";
        String toDate1 = "2020-12-34";
        String page1 = "1fg";
        String pageLimit = "1h";

        Mockito.when(commonValidation.isVallidDateRange(fromDate1, toDate1)).thenReturn(true);
        Mockito.when(commonValidation.isValidPositiveNumber(page1)).thenReturn(true);
        Mockito.when(commonValidation.isValidPositiveNumber(pageLimit)).thenReturn(false);

        mockMvc.perform(get("/v1/customer-orders/order/")
                .param("fromDate", fromDate1).param("toDate", toDate1).param("page", page1).param("limit", pageLimit))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(INVALID_INPUTS))
                .andExpect(jsonPath("$.message").value("Invalid date range or page number"));
    }

    @Test
    void getOrdersTestServerError() throws Exception {
        String fromDate1 = "2020-12-01";
        String toDate1 = "2020-12-31";
        String page1 = "1";
        String pageLimit = "11";

        Mockito.when(orderValidation.isValidGetOrderRequest(fromDate1, toDate1,page1,pageLimit)).thenReturn(true);
//        Mockito.when(commonValidation.isVallidDateRange(fromDate1, toDate1)).thenReturn(true);
//        Mockito.when(commonValidation.isValidPositiveNumber(page1)).thenReturn(true);
//        Mockito.when(commonValidation.isValidPositiveNumber(pageLimit)).thenReturn(true);

        Mockito.doThrow(new DataAccessException("") {
        }).when(orderService).getOrders(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        mockMvc.perform(get("/v1/customer-orders/order/")
                .param("fromDate", fromDate1).param("toDate", toDate1).param("page", page1).param("limit", pageLimit))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(SERVER_ERROR))
                .andExpect(jsonPath("$.message").value(INTERNAL_SERVER_ERROR));
    }

    @Test
    void updateOrderTestSuccess() throws Exception {
        Long orderId = 1000L;
        String orderIdString = "1000";

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest("cancel");

        Mockito.when(orderService.cancelOrder(orderId, orderUpdateRequest)).thenReturn(1);
        Mockito.when(orderValidation.isValidPatchOrderRequest(orderIdString)).thenReturn(true);
//        Mockito.when(commonValidation.isValidPositiveNumber(orderIdString)).thenReturn(true);

        mockMvc.perform(patch("/v1/customer-orders/order/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtilities.asJsonString(orderUpdateRequest)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.status").value(SUCCESS))
                .andExpect(jsonPath("$.message").value("Successful"));
    }

    @Test
    void updateOrderTestBadRequest() throws Exception {
        Long orderId = 1001L;
        String orderIdString = "1000";
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest("fail");

        Mockito.when(orderValidation.isValidPatchOrderRequest(orderIdString)).thenReturn(false);
        Mockito.when(orderService.cancelOrder(orderId, orderUpdateRequest)).thenReturn(0);

        mockMvc.perform(patch("/v1/customer-orders/order/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtilities.asJsonString(orderUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(INVALID_INPUTS))
                .andExpect(jsonPath("$.message").value("Invalid orderId or status"));
    }


    @Test
    void updateOrderTestServerError() throws Exception {
        Long orderId = 1001L;
        String orderIdString = "1001";
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest("cancel");

        Mockito.when(orderService.cancelOrder(anyLong(), any(OrderUpdateRequest.class))).thenThrow(new DataAccessException("") {
        });

//        Mockito.when(commonValidation.isValidPositiveNumber(orderIdString)).thenReturn(true);
        Mockito.when(orderValidation.isValidPatchOrderRequest(orderIdString)).thenReturn(true);

        mockMvc.perform(patch("/v1/customer-orders/order/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtilities.asJsonString(orderUpdateRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(SERVER_ERROR))
                .andExpect(jsonPath("$.message").value(INTERNAL_SERVER_ERROR));
    }

    @Test
    void createOrderTestSuccess() throws Exception {
        Long orderId = 121212L;
        List<ProdQuantity> prodQuantities = new ArrayList<>();
        prodQuantities.add(new ProdQuantity("PROD_1234", 12));
        prodQuantities.add(new ProdQuantity("PROD_1232", 10));

        OrderRequest orderRequest = new OrderRequest(1000L, prodQuantities, "placed", "2020-12-24", 10);

        Mockito.when(orderValidation.isValidOrderRequest(orderRequest)).thenReturn(true);
        Mockito.when(orderService.placeOrder(orderRequest)).thenReturn(orderId);

        mockMvc.perform(post("/v1/customer-orders/order/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtilities.asJsonString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderStatus").value(StatusConst.OrderStatus.placed.toString()))
                .andExpect(jsonPath("$.message").value("Order placed"));
    }

    @Test
    void createOrderTestBadRequest() throws Exception {
        Long orderId = 121212L;
        List<ProdQuantity> prodQuantities = new ArrayList<>();
        prodQuantities.add(new ProdQuantity("PROD_123490", 12));
        prodQuantities.add(new ProdQuantity("PROD_1232huh", 1000000000));

        OrderRequest orderRequest = new OrderRequest(10012121212L, prodQuantities, "fail", "2020-12-24", 10);

        Mockito.when(orderValidation.isValidOrderRequest(orderRequest)).thenReturn(false);
        Mockito.when(orderService.placeOrder(orderRequest)).thenReturn(orderId);

        mockMvc.perform(post("/v1/customer-orders/order/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtilities.asJsonString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.orderStatus").value(StatusConst.OrderStatus.fail.toString()))
                .andExpect(jsonPath("$.message").value("Invalid order request"));

    }

    @Test
    void createOrderTestInterbalError1() throws Exception {
        List<ProdQuantity> prodQuantities = new ArrayList<>();
        prodQuantities.add(new ProdQuantity("PROD_123490", 12));
        prodQuantities.add(new ProdQuantity("PROD_1232huh", 1000000000));

        OrderRequest orderRequest = new OrderRequest(10012121212L, prodQuantities, "fail", "2020-12-24", 10);

        Mockito.when(orderValidation.isValidOrderRequest(orderRequest)).thenReturn(true);
        Mockito.when(orderService.placeOrder(orderRequest)).thenThrow(new DataAccessException("") {
        });

        mockMvc.perform(post("/v1/customer-orders/order/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtilities.asJsonString(orderRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.orderStatus").value(StatusConst.OrderStatus.fail.toString()))
                .andExpect(jsonPath("$.message").value(INTERNAL_SERVER_ERROR));

    }

}
