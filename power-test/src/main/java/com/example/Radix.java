package com.example;

import java.util.ArrayList;
import java.util.List;

public class Radix {

    private final int number;

    public Radix(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Incorrect Value");
        }
        this.number = number;
    }

    public String convertDecimalToAnother(int radix) {
        if (radix < 2 || radix > 16) {
            throw new IllegalArgumentException("Invalid Radix");
        }

        // Trường hợp đặc biệt
        if (number == 0) {
            return "0";
        }

        int n = number;
        List<String> result = new ArrayList<>();

        while (n > 0) {
            int value = n % radix;
            if (value < 10) {
                result.add(String.valueOf(value));
            } else {
                result.add(String.valueOf((char) ('A' + value - 10)));
            }
            n /= radix;
        }

        // đảo ngược kết quả
        StringBuilder sb = new StringBuilder();
        for (int i = result.size() - 1; i >= 0; i--) {
            sb.append(result.get(i));
        }

        return sb.toString();
    }
}
