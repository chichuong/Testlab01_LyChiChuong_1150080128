package com.example.bai7;

import java.util.List;

public class OrderProcessor {

    public double calculateTotal(List<Item> items, String couponCode, String memberLevel, String paymentMethod) {
        if (items == null || items.isEmpty()) { // D1
            throw new IllegalArgumentException("Gio hang trong");
        }

        double subtotal = items.stream().mapToDouble(Item::getPrice).sum();

        // Giam gia theo coupon
        double discount = 0;
        if (couponCode != null && !couponCode.isEmpty()) { // D2
            if (couponCode.equals("SALE10")) { // D3
                discount = subtotal * 0.10;
            } else if (couponCode.equals("SALE20")) { // D4
                discount = subtotal * 0.20;
            } else {
                throw new IllegalArgumentException("Ma giam gia khong hop le");
            }
        }

        // Giam gia theo thanh vien
        double memberDiscount = 0;
        if (memberLevel.equals("GOLD")) { // D5
            memberDiscount = (subtotal - discount) * 0.05;
        } else if (memberLevel.equals("PLATINUM")) { // D6
            memberDiscount = (subtotal - discount) * 0.10;
        }

        double total = subtotal - discount - memberDiscount;

        // Phi ship
        if (total < 500_000) { // D7
            if (!paymentMethod.equals("COD")) { // D8
                total += 30_000; // phi ship online
            } else {
                total += 20_000; // phi ship COD
            }
        }

        return total;
    }
}
