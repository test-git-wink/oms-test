package com.sysco.oms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The type User.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "role")
    private String role;
    @Column(name = "user_password")
    private String userPwd;

}
