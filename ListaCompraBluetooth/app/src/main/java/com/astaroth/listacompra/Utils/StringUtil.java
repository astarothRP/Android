package com.astaroth.listacompra.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rperez on 12/02/2015.
 */
public class StringUtil {
    public static boolean isNullOrEmpty(String s){
        return s==null || s.isEmpty();
    }
    public static boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isEntero(String numero, boolean acceptNullOrEmpty) {
        try {
            if (acceptNullOrEmpty && isNullOrEmpty(numero)) return true;
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
