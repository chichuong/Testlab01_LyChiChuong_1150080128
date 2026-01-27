package com.example;

import javax.swing.SwingUtilities;

public class Bai4Runner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaymentForm().setVisible(true));
    }
}
