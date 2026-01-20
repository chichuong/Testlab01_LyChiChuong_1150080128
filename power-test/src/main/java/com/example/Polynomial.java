package com.example;

import java.util.List;
import java.util.Objects;

public class Polynomial {
    private final int n;
    private final List<Integer> a;

    public Polynomial(int n, List<Integer> a) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid Data");
        }
        if (a == null || a.size() != n + 1) {
            throw new IllegalArgumentException("Invalid Data");
        }
        this.n = n;
        this.a = List.copyOf(a); // immutable copy
    }

    // Tính giá trị đa thức tại x
    public int cal(int x) {
        int result = 0;
        for (int i = 0; i <= n; i++) {
            result += a.get(i) * (int) Math.pow(x, i);
        }
        return result;
    }
}
