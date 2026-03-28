package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MaskingUtil {
    public static String maskCardNumber(Long cardNumber) {
        String formatted = String.format("%04d", cardNumber);
        String tail = formatted.substring(formatted.length() - 4);

        return "**** **** **** %s".formatted(tail);
    }
}
