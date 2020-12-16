package com.sysco.oms.repository;

import org.springframework.stereotype.Repository;


@Repository
public interface UserAddressRepo  {

    Long countUserByUserAddressId(Integer userAddressId,Long userId);
}
