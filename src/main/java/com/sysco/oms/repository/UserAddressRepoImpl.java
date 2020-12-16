package com.sysco.oms.repository;


import com.sysco.oms.model.UserAddress;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class UserAddressRepoImpl implements UserAddressRepo{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long countUserByUserAddressId(Integer userAddressId, Long userId) {
        CriteriaBuilder criteriaBuilder= entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<UserAddress> userAddress=criteriaQuery.from(UserAddress.class);

        criteriaQuery.select(criteriaBuilder.count(userAddress));
        Predicate getUserAddressId=criteriaBuilder.equal(userAddress.get("custAddressID"),userAddressId);
        Predicate getUserId=criteriaBuilder.equal(userAddress.get("userId"),userId);
        criteriaQuery.where(getUserAddressId,getUserId);
        TypedQuery<Long> query=entityManager.createQuery(criteriaQuery);

        return query.getSingleResult();
    }
}
