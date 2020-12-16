package com.sysco.oms.repository;

import com.sysco.oms.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductsRepo extends JpaRepository<Product,String> {

    @Transactional
    @Modifying
    @Query(value = "Update product  set in_stock_quantity = in_stock_quantity - ?2 where product_id = ?1 ",nativeQuery = true)
    int updateProductQuantity(String productId, Float quantity);

    @Query(value = "SELECT p.productSellPrice FROM Product p WHERE p.productId= ?1 ")
    Double findProductPriceById(String productId);
}
