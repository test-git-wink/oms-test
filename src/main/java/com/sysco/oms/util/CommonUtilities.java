package com.sysco.oms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sysco.oms.validation.CommonValidation;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.sysco.oms.constants.CommonConst.DATE_PATTERN;

@Component
public class CommonUtilities {


    private static final CommonValidation commonValidation = new CommonValidation();
    private static final SecureRandom random = new SecureRandom();

    private CommonUtilities() {
    }

    public static Date convertStringToDate(String date)  {

        try {
            return new SimpleDateFormat(DATE_PATTERN).parse(date);
        } catch (Exception e) {
            return null;
        }


    }

    public static Date getStartofDay(Date date) {
        DateTime startOfDay = new DateTime(date).withTimeAtStartOfDay();
        return startOfDay.toDate();
    }

    public static Date getEndofDay(Date date) {
        DateTime endOfDay = new DateTime(date).plusDays(1).withTimeAtStartOfDay();
        return endOfDay.toDate();
    }

    public static LocalDate convertStringToLocaldate(String date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_PATTERN);

        if (commonValidation.isValidDate(date))
            return LocalDate.parse(date, format);
        else
            return null;

    }

    public static Date getCurrentTimeStamp() {
        return new Date();
    }

    public static String getInvoiceId() {
        int bound = 10;
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(bound));
        }
        return id.toString();

    }


    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
