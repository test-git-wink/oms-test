package com.sysco.oms.util;

import com.sysco.oms.constants.StatusConst;
import com.sysco.oms.validation.CommonValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sysco.oms.constants.CommonConst.DELIVERY_DATE;
import static com.sysco.oms.util.CommonUtilities.*;

/**
 * The type Order utilites.
 */
@Component
public class OrderUtilites {


    @Autowired
    private CommonValidation commonValidation;

    /**
     * Gets order search date range date.
     *
     * @param from the from
     * @param to   the to
     * @return the order search date range date
     * @throws ParseException the parse exception
     */
    public Map<String, Date> getOrderSearchDateRangeDate(String from, String to)  {

        HashMap<String, Date> dates = new HashMap<>();

        Date fromDate = getStartofDay(convertStringToDate(from));
        Date toDate = getEndofDay(convertStringToDate(to));

        dates.put("fromDate", fromDate);
        dates.put("toDate", toDate);
        return dates;

    }

    public LocalDate getDeliveryDate() {
        return LocalDate.now().plusDays(DELIVERY_DATE);
    }

    public StatusConst.OrderStatus getEnumOrderStatus(String orderStatus) {
        for (StatusConst.OrderStatus val : StatusConst.OrderStatus.values()) {
            if (val.toString().equals(orderStatus))
                return val;

        }
        return null;
    }
}
