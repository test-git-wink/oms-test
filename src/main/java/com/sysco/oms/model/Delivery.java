package com.sysco.oms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * The type Delivery.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delivery_id")
    private Integer deliveryId;
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;
    @Column(name = "delivery_status")
    private String deliveryStatus;
    @Column(name = "user_address_id")
    private Integer userAddressId;

    @OneToOne
    @JoinColumn(name = "user_address_id",insertable = false,updatable = false)
    private UserAddress userAddress;


    /**
     * Instantiates a new Delivery.
     *
     * @param deliveryDate   the delivery date
     * @param deliveryStatus the delivery status
     * @param userAddressId  the user address id
     */
    public Delivery(LocalDate deliveryDate, String deliveryStatus, Integer userAddressId) {
        this.deliveryDate = deliveryDate;
        this.deliveryStatus = deliveryStatus;
        this.userAddressId = userAddressId;
    }
}
