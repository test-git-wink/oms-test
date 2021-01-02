package com.sysco.oms.service;

import com.sysco.oms.model.*;
import com.sysco.oms.model.requests.OrderRequest;
import com.sysco.oms.model.requests.OrderUpdateRequest;
import com.sysco.oms.repository.*;
import com.sysco.oms.util.OrderUtilites;
import com.sysco.oms.constants.StatusConst;
import com.sysco.oms.validation.CommonValidation;
import com.sysco.oms.validation.OrderValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepo orderRepo;

    @MockBean
    private CommonValidation commonValidation;

    @MockBean
    private DeliveryCustomRepo deliveryCustomRepo;

    @MockBean
    private OrderCustomRepo orderCustomRepo;

    @MockBean
    private OrderItemRepo orderItemRepo;

    @MockBean
    private OrderValidation orderValidation;

    @MockBean
    private OrderUtilites orderUtilites;

    @MockBean
    private ProductsRepo productsRepo;

    @Test
    void placeOrder() {
        int deliveryId1 = 1;
        Long orderId1 = 100L;
        List<ProdQuantity> prodQuantities1 = new ArrayList<>();
        prodQuantities1.add(new ProdQuantity("PROD_1234", 12));
        prodQuantities1.add(new ProdQuantity("PROD_1232", 10));

        Product product1 = new Product("PROD_1234", "PROD_1234_name"
                , "grain", 100.0, 105.0, 1000, "Kg");
        Product product2 = new Product("PROD_1232", "PROD_1232_name"
                , "grain", 100.0, 105.0, 1000, "Kg");

        OrderItem orderItem1 = new OrderItem(12.0F, 120.0, "PROD_1234");
        OrderItem orderItem2 = new OrderItem(12F, 120.0, "PROD_1232");

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem1);
        orderItemList.add(orderItem2);

        OrderRequest orderRequest1 =
                new OrderRequest(10L, prodQuantities1, StatusConst.OrderStatus.placed.toString(), "2020-12-31", 1);

        when(orderValidation.validOrderItemList(prodQuantities1)).thenReturn(prodQuantities1);

        when(orderUtilites.getDeliveryDate()).thenReturn(LocalDate.now().plusDays(7));

        when(productsRepo.findProductPriceById(product1.getProductId())).thenReturn(product1.getProductSellPrice());
        when(productsRepo.findProductPriceById(product2.getProductId())).thenReturn(product2.getProductSellPrice());

        when(orderCustomRepo.insertOrder(any(OrderRequest.class),any(Integer.class),any(String.class),any(Date.class),any(Double.class))).thenReturn(orderId1);

        when(orderItemRepo.saveAll(orderItemList)).thenReturn(orderItemList);
        when(productsRepo.updateProductQuantity(any(String.class),any(Float.class))).thenReturn(1);


        Assertions.assertEquals(orderId1, orderService.placeOrder(orderRequest1));
    }

    @Test
    void getOrders() throws Exception {
        SimpleDateFormat formatterWithTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);


        String from1 = "2020-12-01";
        String to1 = "2020-12-31";
        Map<String, Date> orderSearchDate1=new HashMap<>();
        orderSearchDate1.put("fromDate",formatterWithTime.parse("2020-12-01 00:00:00"));
        orderSearchDate1.put("toDate",formatterWithTime.parse("2021-01-01 00:00:00"));

        String from2 = "2020-12-01";
        String to2 = "2020-10-31";
        Map<String, Date> orderSearchDate2=new HashMap<>();
        orderSearchDate2.put("fromDate",formatterWithTime.parse("2020-12-01 00:00:00"));
        orderSearchDate2.put("toDate",formatterWithTime.parse("2020-11-01 00:00:00"));


        List<Order> orders1 = new ArrayList<>();
        List<Order> orders2 = new ArrayList<>();

        orders1.add(new Order(1000L, 100.0, formatterWithTime.parse("2020-12-05 12:00:00"), StatusConst.OrderStatus.placed, "0011",
                1, 10L, new User(10L, "username 1", "admin", "Asdwed1212")
                , new Delivery(1, LocalDate.of(2020, 12, 16), "pending", 10
                , new UserAddress(1L, 10L, "89", "Abc street", "", "", "ab")
        )));
        orders1.add(new Order(1001L, 100.0, formatterWithTime.parse("2020-12-06 12:00:00"), StatusConst.OrderStatus.placed, "0012",
                1, 10L, new User(10L, "username 1", "admin", "Asdwed1212")
                , new Delivery(1, LocalDate.of(2020, 12, 16), "pending", 10
                , new UserAddress(1L, 10L, "89", "Abc street", "", "", "ab")
        )));

        Page<Order> orderPage1 = new PageImpl<>(orders1);
        Page<Order> orderPage2 = new PageImpl<>(orders2);

        List<OrderData> orderData1 = new ArrayList<>();
        List<OrderData> orderData2 = new ArrayList<>();

        orderData1.add(new OrderData(1000L, 10L, 100.0
                , formatterWithTime.parse("2020-12-05 12:00:00"), "placed", LocalDate.of(2020, 12, 16), "pending", "89 , Abc street , ab"));
        orderData1.add(new OrderData(1001L, 10L, 100.0
                , formatterWithTime.parse("2020-12-06 12:00:00"), "placed", LocalDate.of(2020, 12, 16), "pending", "89 , Abc street , ab"));

        when(orderUtilites.getOrderSearchDateRangeDate(from1, to1)).thenReturn(orderSearchDate1);
        when(orderUtilites.getOrderSearchDateRangeDate(from2, to2)).thenReturn(orderSearchDate2);

        when(orderRepo.findOrderDataByDateRange(any(Date.class), any(Date.class), any(Pageable.class))).thenReturn(orderPage1);

//        when(orderRepo.findOrderDataByDateRange(formatterWithTime.parse("2020-12-01 00:00:00"), formatterWithTime.parse("2020-11-01 00:00:00"), PageRequest.of(1,2))).thenThrow(new DataAccessException("..."){ });

        Assertions.assertEquals(orderData1, orderService.getOrders(from1, to1, "1", "2"));
//        Assertions.assertEquals(new ArrayList<>(), orderService.getOrders("2020-12-01", "2020-10-31", 1, 2));
    }

    @Test
    void cancelOrderTest() {
        Long orderId1 = 100L;
        Long orderId2 = 101L;
        Long orderId3 = 102L;
        Long orderId4 = 103L;
        Long orderId5 = 104L;
        Long orderId6 = 105L;

        OrderUpdateRequest orderUpdateRequest1 = new OrderUpdateRequest("cancel");
        OrderUpdateRequest orderUpdateRequest2 = new OrderUpdateRequest("fail");
        OrderUpdateRequest orderUpdateRequest3 = new OrderUpdateRequest("abc");
        OrderUpdateRequest orderUpdateRequest4 = new OrderUpdateRequest("approved");
        OrderUpdateRequest orderUpdateRequest5 = new OrderUpdateRequest("cancel");
        OrderUpdateRequest orderUpdateRequest6 = new OrderUpdateRequest("cancel");

        when(orderRepo.findOrderStatusById(orderId1)).thenReturn("approved");
        when(orderRepo.findOrderStatusById(orderId2)).thenReturn("placed");
        when(orderRepo.findOrderStatusById(orderId3)).thenReturn("approved");
        when(orderRepo.findOrderStatusById(orderId4)).thenReturn("processing");
        when(orderRepo.findOrderStatusById(orderId5)).thenReturn("fail");
        when(orderRepo.findOrderStatusById(orderId6)).thenReturn("cancel");

        when(orderValidation.isValidOrderCancelRequest(orderId1,orderUpdateRequest1)).thenReturn(true);
        when(orderValidation.isValidOrderCancelRequest(orderId2,orderUpdateRequest2)).thenReturn(false);
        when(orderValidation.isValidOrderCancelRequest(orderId3,orderUpdateRequest3)).thenReturn(false);
        when(orderValidation.isValidOrderCancelRequest(orderId4,orderUpdateRequest4)).thenReturn(false);
        when(orderValidation.isValidOrderCancelRequest(orderId5,orderUpdateRequest5)).thenReturn(true);
        when(orderValidation.isValidOrderCancelRequest(orderId6,orderUpdateRequest6)).thenReturn(true);

        when(orderRepo.updateOrderStatus(orderId1, StatusConst.OrderStatus.cancel.toString())).thenReturn(1);

        Assertions.assertTrue(orderService.cancelOrder(orderId1, orderUpdateRequest1) > 0);
        Assertions.assertFalse(orderService.cancelOrder(orderId2, orderUpdateRequest2) > 0);
        Assertions.assertFalse(orderService.cancelOrder(orderId3, orderUpdateRequest3) > 0);
        Assertions.assertFalse(orderService.cancelOrder(orderId4, orderUpdateRequest4) > 0);
        Assertions.assertFalse(orderService.cancelOrder(orderId5, orderUpdateRequest5) > 0);
        Assertions.assertFalse(orderService.cancelOrder(orderId6, orderUpdateRequest6) > 0);

    }


    @Test
    void addDeliveryTest() {
        LocalDate deliveryDate = LocalDate.now();
        String deliveryStatus = "pending";
        int userAddressId = 1;
        int deliveryId = 100;

        when(deliveryCustomRepo.insertDelivery(deliveryDate, userAddressId, deliveryStatus)).thenReturn(deliveryId);

        Assertions.assertTrue(orderService.addDelivery(deliveryDate, userAddressId, deliveryStatus) > 0);

    }

}