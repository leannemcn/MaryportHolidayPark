package com.example.b00682737.maryportholidaypark.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String DATE_FORMAT_1 = "MM/dd/yyyy";

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String toStringFormat_1(Date date) {
        if (date == null)
            return "";
        return dateToString(date, DATE_FORMAT_1);
    }

    public static int getYearFromTimestamp(long timestamp)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);

        return c.get(Calendar.YEAR);
    }

    public static int getMonthFromTimestamp(long timestamp)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);

        return c.get(Calendar.MONTH);
    }

    public static int getDayFromTimestamp(long timestamp)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);

        return c.get(Calendar.DAY_OF_MONTH);
    }
}
