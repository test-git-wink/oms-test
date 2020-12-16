package com.sysco.oms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The type User address.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_address")
public class UserAddress {
    @Id
    @Column(name = "user_address_id")
    private Long custAddressID;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "street_number")
    private String streetNumber;
    @Column(name = "street")
    private String street;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "country")
    private String country;

    /**
     * Gets concat address.
     *
     * @return the concat address
     */
    public String getConcatAddress() {
        String address="";
        if (!this.streetNumber.equals(""))
            address+=this.streetNumber;
        if (!this.street.equals(""))
            address+=" , "+this.street;
        if (!this.city.equals(""))
            address+=" , "+this.city;
        if (!this.state.equals(""))
            address+=" , "+this.state;
        if (!this.country.equals(""))
            address+=" , "+this.country;

        return address;
    }
}
