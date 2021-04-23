package br.com.chamasindico.utils;

import java.util.Collection;

public class Utils {

    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }
}
