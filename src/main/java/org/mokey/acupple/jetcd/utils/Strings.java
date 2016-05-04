package org.mokey.acupple.jetcd.utils;

/**
 * Created by enousei on 4/21/16.
 */
public class Strings {
    public static boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty();
    }
    public static String add(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            if (str == null) {
                continue;
            }
            sb.append(str);
        }
        return sb.toString();
    }
}
