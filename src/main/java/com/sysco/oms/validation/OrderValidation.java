package com.sysco.oms.validation;

import com.sysco.oms.model.ProdQuantity;
import com.sysco.oms.model.Product;
import com.sysco.oms.model.requests.OrderRequest;
import com.sysco.oms.model.requests.OrderUpdateRequest;
import com.sysco.oms.repository.OrderRepo;
import com.sysco.oms.repository.ProductsRepo;
import com.sysco.oms.repository.UserAddressRepo;
import com.sysco.oms.repository.UserRepo;
import com.sysco.oms.constants.StatusConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderValidation {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserAddressRepo userAddressRepo;
    @Autowired
    private ProductsRepo productsRepo;
    @Autowired
    private CommonValidation commonValidation;


    public boolean isValidOrder(Long orderId) {
        return orderRepo.countByOrderId(orderId) > 0;
    }

    public boolean isValidUpdateStatus(OrderUpdateRequest orderUpdateRequest) {
        for (StatusConst.OrderStatus orderStatus : StatusConst.OrderStatus.values()) {
            if (orderUpdateRequest.getOrderStatus().equals(orderStatus.toString()))
                return true;
        }
        return false;
    }

    public boolean isValidUserAddress(Integer userAddrressID, Long userID) {
        Long val = userAddressRepo.countUserByUserAddressId(userAddrressID, userID);
        return val > 0;
    }

    public boolean isValidOrderRequestStatus(String orderStatus) {
        for (StatusConst.OrderRequestStatus orderRequestStatus : StatusConst.OrderRequestStatus.values()) {
            if (orderRequestStatus.toString().equals(orderStatus))
                return true;
        }
        return false;
    }

    public List<ProdQuantity> validOrderItemList(List<ProdQuantity> itemList) {
        List<ProdQuantity> orderingProducts = new ArrayList<>();
        for (ProdQuantity prodQuantity : itemList) {
            Optional<Product> product = productsRepo.findById(prodQuantity.getProductId());

            if (product.isPresent() && product.get().getInStockQuantiy() > prodQuantity.getQuantity()) {
                orderingProducts.add(prodQuantity);
            }
        }
        return orderingProducts;
    }

    public boolean isValidOrderRequest(OrderRequest orderRequest) {
        boolean validUserAddress = isValidUserAddress(orderRequest.getUserAddresID(), orderRequest.getUserId());
        boolean validOrderStatus = isValidOrderRequestStatus(orderRequest.getOrderStatus());
        int orderingProducts = validOrderItemList(orderRequest.getOrderItemList()).size();

        return validUserAddress && validOrderStatus && orderingProducts > 0;
    }

    public boolean isValidGetOrderRequest(String fromDate, String toDate, String page, String limit) {
        return commonValidation.isVallidDateRange(fromDate, toDate)
                && commonValidation.isValidPositiveNumber(page) && commonValidation.isValidPositiveNumber(limit);
    }

    public boolean isValidPatchOrderRequest(String orderId) {
        return commonValidation.isValidPositiveNumber(orderId) && isValidOrder(Long.parseLong(orderId));
    }

    public boolean isValidOrderCancelRequest(Long orderId,OrderUpdateRequest orderUpdateRequest) {
        String status = orderRepo.findOrderStatusById(orderId);

        return isValidUpdateStatus(orderUpdateRequest) &&
                !status.equals(StatusConst.OrderStatus.fail.toString()) && !status.equals(StatusConst.OrderStatus.cancel.toString())
                && orderUpdateRequest.getOrderStatus().equals(StatusConst.OrderStatus.cancel.toString());
    }

}
