package com.rperez.ongpersonas.personasatendidas.utils;

import java.util.Calendar;

/**
 * Created by rperez on 09/02/2015.
 */
public class CalendarUtil {
    public static long cleanCal(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
