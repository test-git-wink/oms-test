package com.sysco.oms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sysco.oms.model.requests.OrderUpdateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

class CommonUtilitiesTest {


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    SimpleDateFormat formatterWithTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);

    @Test
    void convertStringToDateTest() throws ParseException {
        Assertions.assertEquals(formatter.parse("2020-12-13"), CommonUtilities.convertStringToDate("2020-12-13"));
        Assertions.assertNotEquals(formatter.parse("2020-12-12"), CommonUtilities.convertStringToDate("2020-12-13"));
        Assertions.assertNotEquals(formatter.parse("2020-11-13"), CommonUtilities.convertStringToDate("2020-nov-13"));
        Assertions.assertNotEquals(formatter.parse("2020-11-13"), CommonUtilities.convertStringToDate("hi"));
    }

    @Test
    void getStartofDayTest() throws ParseException {
        Assertions.assertEquals(formatterWithTime.parse("2020-12-12 00:00:00"),CommonUtilities.getStartofDay(formatterWithTime.parse("2020-12-12 12:00:00")));
        Assertions.assertNotEquals(formatterWithTime.parse("2020-12-13 23:00:00"),CommonUtilities.getStartofDay(formatterWithTime.parse("2020-12-13 23:00:00")));
    }

    @Test
    void getEndofDayTest() throws ParseException {
        Assertions.assertEquals(formatterWithTime.parse("2020-12-13 00:00:00"),CommonUtilities.getEndofDay(formatterWithTime.parse("2020-12-12 12:00:00")));
        Assertions.assertNotEquals(formatterWithTime.parse("2020-12-12 23:00:00"),CommonUtilities.getStartofDay(formatterWithTime.parse("2020-12-12 23:00:00")));
    }

    @Test
    void convertStringToLocaldate() {
        Assertions.assertEquals(LocalDate.of(2020,12,13),CommonUtilities.convertStringToLocaldate("2020-12-13"));
        Assertions.assertNotEquals(LocalDate.of(2020,12,11),CommonUtilities.convertStringToLocaldate("2020-12-13"));
        Assertions.assertNotEquals(LocalDate.of(2020,12,11),CommonUtilities.convertStringToLocaldate("hiihi"));
    }

    @Test
    void getInvoiceIdTest()  {
        Assertions.assertEquals(10,CommonUtilities.getInvoiceId().length());
        Assertions.assertNotEquals(CommonUtilities.getInvoiceId(),CommonUtilities.getInvoiceId());
    }

    @Test
    void asJsonString() throws JsonProcessingException {
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest("cancel");

        Assertions.assertEquals("{\"orderStatus\":\"cancel\"}",CommonUtilities.asJsonString(orderUpdateRequest));
        Assertions.assertNotEquals("{\"orderStatus\":\"cancel\"}",CommonUtilities.asJsonString(Float.class));
    }
}
