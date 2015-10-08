package com.ntilde.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 3003012 on 08/10/2015.
 */
public class Utils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * Convierte una cadena con el formato yyyy-MM-dd HH:mm a una objeto de tipo fecha
     * @param dateString
     * @return
     */
    public static Date convertStringToDate(String dateString){

        if(dateString != null && !dateString.isEmpty()) {
            try {
                Date date = DATE_FORMAT.parse(dateString);
                return date;
            } catch (ParseException e) {
                //Error en el parseo
            }
        }
        return null;
    }

    /**
     * Convierte objeto date a cadena con el formato yyyy-MM-dd HH:mm
     * @param date
     * @return
     */
    public static String convertDateToString(Date date){

        String dateString = DATE_FORMAT.format(date);
        return dateString;

    }
}
