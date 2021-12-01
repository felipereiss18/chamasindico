package br.com.chamasindico.utils;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class Utils {

    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static String localDateToString(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return data.format(formatter);
    }
}
