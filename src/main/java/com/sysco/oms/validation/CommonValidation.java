package com.sysco.oms.validation;

import com.sysco.oms.util.CommonUtilities;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.sysco.oms.constants.CommonConst.DATE_PATTERN;

@Component
public class CommonValidation {


    public boolean isValidDate(String dateToValidate) {

        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_PATTERN);
            fmt.parseDateTime(dateToValidate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean isValidPositiveNumber(String num) {
        try {
            long number = Long.parseLong(num);
            return number >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isVallidDateRange(String from, String to) {

        if (isValidDate(from) && isValidDate(to)) {
            LocalDate fromDate = CommonUtilities.convertStringToLocaldate(from);
            LocalDate toDate = CommonUtilities.convertStringToLocaldate(to);

            return fromDate.isBefore(toDate) || fromDate.isEqual(toDate);
        } else
            return false;
    }
}
