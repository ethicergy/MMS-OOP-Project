package com.mms.util;

public class InputValidator {
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isValidLength(String s, int min, int max) {
        if (s == null) return false;
        int len = s.trim().length();
        return len >= min && len <= max;
    }

    // Add more validation methods as needed
}
