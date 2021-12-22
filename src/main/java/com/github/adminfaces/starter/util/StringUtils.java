package com.github.adminfaces.starter.util;

import java.util.Base64;

public class StringUtils {

    public static String encodeString(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }

    public static String decodeString(String string) {
        return new String(Base64.getDecoder().decode(string));
    }
}
