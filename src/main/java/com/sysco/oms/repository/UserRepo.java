package com.sysco.oms.repository;

import com.sysco.oms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    @Query(countQuery = "SELECT count(user_id) from User where user_id = ?1")
    Integer findValidUserByUserId(String userId);
}
