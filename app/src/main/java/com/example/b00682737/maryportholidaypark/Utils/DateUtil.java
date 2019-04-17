package com.example.b00682737.maryportholidaypark.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {


    public static final String DATE_FORMAT_12 = "yyyy-MM-dd";

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parseDataFromFormat12(String dateString) {
        Date retDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_12);
        try {
            retDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retDate;
    }
}
