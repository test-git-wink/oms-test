package com.sysco.oms.validation;

import com.sysco.oms.model.ProdQuantity;
import com.sysco.oms.model.Product;
import com.sysco.oms.model.requests.OrderRequest;
import com.sysco.oms.model.requests.OrderUpdateRequest;
import com.sysco.oms.repository.OrderRepo;
import com.sysco.oms.repository.ProductsRepo;
import com.sysco.oms.repository.UserAddressRepo;
import com.sysco.oms.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderValidationTest {

    @Autowired
    private OrderValidation orderValidation;

    @MockBean
    private UserRepo userRepo;
    @MockBean
    private OrderRepo orderRepo;
    @MockBean
    private UserAddressRepo userAddressRepo;
    @MockBean
    private ProductsRepo productsRepo;

    @Test
    void isValidOrderTest() {
        Long orderId=1000L;
        when(orderRepo.countByOrderId(orderId))
                .thenReturn(1L);

        Assertions.assertTrue(orderValidation.isValidOrder(1000L));
        Assertions.assertFalse(orderValidation.isValidOrder(1001L));
    }

    @Test
    void isValidUserAddressTest() {
        int userAddressId=10;
        Long userId=1000L;
        when(userAddressRepo.countUserByUserAddressId(userAddressId,userId))
                .thenReturn(1L);

        Assertions.assertTrue(orderValidation.isValidUserAddress(10,1000L));
        Assertions.assertFalse(orderValidation.isValidUserAddress(100,1000L));
    }

    @Test
    void isValidUpdateStatusTest(){
        OrderUpdateRequest orderUpdateRequest1 = new OrderUpdateRequest("placed");
        OrderUpdateRequest orderUpdateRequest2 = new OrderUpdateRequest("approved");
        OrderUpdateRequest orderUpdateRequest3 = new OrderUpdateRequest("place");
        OrderUpdateRequest orderUpdateRequest4 = new OrderUpdateRequest("processing");
        OrderUpdateRequest orderUpdateRequest5 = new OrderUpdateRequest("fail");
        OrderUpdateRequest orderUpdateRequest6 = new OrderUpdateRequest("placed");
        OrderUpdateRequest orderUpdateRequest7 = new OrderUpdateRequest("cancel");

        Assertions.assertTrue(orderValidation.isValidUpdateStatus(orderUpdateRequest1));
        Assertions.assertTrue(orderValidation.isValidUpdateStatus(orderUpdateRequest2));
        Assertions.assertFalse(orderValidation.isValidUpdateStatus(orderUpdateRequest3));
        Assertions.assertTrue(orderValidation.isValidUpdateStatus(orderUpdateRequest4));
        Assertions.assertTrue(orderValidation.isValidUpdateStatus(orderUpdateRequest5));
        Assertions.assertTrue(orderValidation.isValidUpdateStatus(orderUpdateRequest6));
        Assertions.assertTrue(orderValidation.isValidUpdateStatus(orderUpdateRequest7));
    }

    @Test
    void isValidOrderRequestStatusTest(){
        Assertions.assertTrue(orderValidation.isValidOrderRequestStatus("cancel"));
        Assertions.assertTrue(orderValidation.isValidOrderRequestStatus("placed"));
        Assertions.assertFalse(orderValidation.isValidOrderRequestStatus("place"));
    }

    @Test
    void validOrderItemListTest(){
        String prodId1="PROD_1234";
        String prodId2="PROD_1235";
        when(productsRepo.findById(prodId1))
                .thenReturn(java.util.Optional.of(new Product("PROD_1234", "PROD_1234_name"
                        , "grain", 100.0, 105.0, 1000, "Kg")));
        when(productsRepo.findById(prodId2))
                .thenReturn(java.util.Optional.of(new Product("PROD_1235", "PROD_1235_name"
                        , "grain", 10.0, 15.0, 1000, "Kg")));

        List<ProdQuantity> itemList1=new ArrayList<>();
        itemList1.add(new ProdQuantity(prodId1,10));
        itemList1.add(new ProdQuantity(prodId2,5));

        List<ProdQuantity> itemList2=new ArrayList<>();
        itemList2.add(new ProdQuantity("PROD_1111",10));
        itemList2.add(new ProdQuantity("PROD_1122",5));

        List<ProdQuantity> itemList3=new ArrayList<>();
        itemList3.add(new ProdQuantity("PROD_1234",100000));
        itemList3.add(new ProdQuantity("PROD_1235",500000));

        Assertions.assertEquals(itemList1,orderValidation.validOrderItemList(itemList1));
        Assertions.assertNotEquals(itemList2,orderValidation.validOrderItemList(itemList2));
        Assertions.assertNotEquals(itemList3,orderValidation.validOrderItemList(itemList3));
    }

    @Test
    void isValidOrderRequest() {
        String prodId1="PROD_1234";
        String prodId2="PROD_1244";
        List<ProdQuantity> itemList1=new ArrayList<>();
        List<ProdQuantity> itemList2=new ArrayList<>();
        itemList1.add(new ProdQuantity("PROD_1234",10));
        itemList1.add(new ProdQuantity(prodId2,10));
        OrderRequest orderRequest=new OrderRequest(100L,itemList1,"placed","2020-12-31",11);
        OrderRequest orderRequest1=new OrderRequest(100L,itemList2,"placed","2020-12-31",11);
        OrderRequest orderRequest2=new OrderRequest(100L,itemList2,"placed","2020-12-31",11);
        OrderRequest orderRequest3=new OrderRequest(100L,itemList1,"fail","2020-12-31",11);

        when(productsRepo.findById(prodId1))
                .thenReturn(java.util.Optional.of(new Product("PROD_1234", "PROD_1234_name"
                        , "grain", 100.0, 105.0, 1000, "Kg")));
        when(productsRepo.findById(prodId2))
                .thenReturn(java.util.Optional.of(new Product()));
        when(userAddressRepo.countUserByUserAddressId(any(Integer.class),any(Long.class)))
                .thenReturn(1L);

        Assertions.assertTrue(orderValidation.isValidOrderRequest(orderRequest));

        Assertions.assertFalse(orderValidation.isValidOrderRequest(orderRequest1));
        Assertions.assertFalse(orderValidation.isValidOrderRequest(orderRequest2));
        Assertions.assertFalse(orderValidation.isValidOrderRequest(orderRequest3));
    }
}
