package com.sysco.oms.util;

import com.sysco.oms.constants.StatusConst;
import com.sysco.oms.validation.CommonValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.sysco.oms.constants.CommonConst.DELIVERY_DATE;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderUtiliesTest {


    @Autowired
    private OrderUtilites orderUtilites;

    @MockBean
    private CommonValidation commonValidation;

    @Test
    void getEnumOrderStatusTest(){
        Assertions.assertEquals(StatusConst.OrderStatus.placed,orderUtilites.getEnumOrderStatus("placed"));
        Assertions.assertEquals(StatusConst.OrderStatus.fail,orderUtilites.getEnumOrderStatus("fail"));
        Assertions.assertNotEquals(StatusConst.OrderStatus.placed,orderUtilites.getEnumOrderStatus("place"));
    }

    @Test
    void getDeliveryDateTest(){
        Assertions.assertEquals(LocalDate.now().plusDays(DELIVERY_DATE),orderUtilites.getDeliveryDate());
        Assertions.assertNotNull(orderUtilites.getDeliveryDate());
        Assertions.assertNotNull(orderUtilites.getDeliveryDate());
    }

    @Test
    void getOrderSearchDateRangeDateTest() throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String from1="2020-12-14";
        String to1="2020-01-01";
        String from2="2020-12-14";
        String to2="2020-12-24";
        when(commonValidation.isVallidDateRange(from1,to1)).thenReturn(false);
        when(commonValidation.isVallidDateRange(from2,"2020-12-23")).thenReturn(true);

        HashMap<String, Date> expected1=new HashMap<>();
        expected1.put("toDate",formatter.parse(from1));
        expected1.put("fromDate",formatter.parse(to1));

        HashMap<String, Date> expected2=new HashMap<>();
        expected2.put("toDate",formatter.parse(to2));
        expected2.put("fromDate",formatter.parse(from2));

        Map<String,Date> actual=orderUtilites.getOrderSearchDateRangeDate("2020-12-14","2020-12-23");

        Assertions.assertEquals(expected2.size(),actual.size());
        Assertions.assertEquals(expected2.get("fromDate"),actual.get("fromDate"));
        Assertions.assertEquals(expected2.get("toDate"),actual.get("toDate"));

        Assertions.assertNotEquals(expected1,orderUtilites.getOrderSearchDateRangeDate(from1,to1));

    }
}
