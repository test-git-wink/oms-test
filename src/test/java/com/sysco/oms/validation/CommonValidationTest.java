package com.sysco.oms.validation;

import com.sysco.oms.validation.CommonValidation;
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
        Assertions.assertTrue(commonValidation.isValidNumber("2020"));
        Assertions.assertFalse(commonValidation.isValidNumber("2020.3"));
        Assertions.assertFalse(commonValidation.isValidNumber("Hi"));
    }

    @Test
    void isVallidDateRangeTest() {
        Assertions.assertTrue(commonValidation.isVallidDateRange("2020-12-01","2020-12-31"));
        Assertions.assertTrue(commonValidation.isVallidDateRange("2020-12-01","2021-01-31"));
        Assertions.assertTrue(commonValidation.isVallidDateRange("2020-12-01","2020-12-01"));
        Assertions.assertFalse(commonValidation.isVallidDateRange("2020-12-01","2019-01-31"));
        Assertions.assertFalse(commonValidation.isVallidDateRange("2020-13-01","2020-14-31"));
        Assertions.assertFalse(commonValidation.isVallidDateRange("Hi","234asd"));
        Assertions.assertFalse(commonValidation.isVallidDateRange(null,null));
    }
}
