package com.sysco.oms.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommonValidationTest {

    CommonValidation commonValidation =new CommonValidation();

    @Test
    void isValidDateTest() {
        Assertions.assertTrue(commonValidation.isValidDate("2020-12-12"));
        Assertions.assertFalse(commonValidation.isValidDate("2020-13-12"));
        Assertions.assertFalse(commonValidation.isValidDate("2020-13-58"));
        Assertions.assertFalse(commonValidation.isValidDate("2020-nov-58"));
    }

    @Test
    void isValidNumberTest() {
        Assertions.assertTrue(commonValidation.isValidPositiveNumber("2020"));
        Assertions.assertFalse(commonValidation.isValidPositiveNumber("2020.3"));
        Assertions.assertFalse(commonValidation.isValidPositiveNumber("Hi"));
        Assertions.assertTrue(commonValidation.isValidPositiveNumber("0"));
        Assertions.assertFalse(commonValidation.isValidPositiveNumber("-3"));
    }

    @Test
    void isVallidDateRangeTest() {
        Assertions.assertTrue(commonValidation.isVallidDateRange("2020-12-01","2020-12-31"));
        Assertions.assertTrue(commonValidation.isVallidDateRange("2020-12-01","2021-01-31"));
        Assertions.assertTrue(commonValidation.isVallidDateRange("2020-12-01","2020-12-01"));
        Assertions.assertFalse(commonValidation.isVallidDateRange("2020-12-01","2019-01-31"));
        Assertions.assertFalse(commonValidation.isVallidDateRange("2020-13-01","2020-14-31"));
        Assertions.assertFalse(commonValidation.isVallidDateRange("Hi","234asd"));
        Assertions.assertFalse(commonValidation.isVallidDateRange("Hi","2020-13-01"));
        Assertions.assertFalse(commonValidation.isVallidDateRange("2020-14-31","234asd"));
        Assertions.assertFalse(commonValidation.isVallidDateRange("2020-14-31",null));
        Assertions.assertFalse(commonValidation.isVallidDateRange(null,"234asd"));
        Assertions.assertFalse(commonValidation.isVallidDateRange("2020-14-31",null));
        Assertions.assertFalse(commonValidation.isVallidDateRange(null,"2020-14-31"));
        Assertions.assertFalse(commonValidation.isVallidDateRange(null,null));
    }
}
