package com.peshkov;

import com.peshkov.Matrices.*;

public class Main {
    public static void main(String[] args) {
        var e = new GMatrix<>(5, 5, i -> (i % 5) * (i / 5));
        System.out.println(e);
        System.out.println(e.mul(e, Integer::sum, (a, b) -> a * b));
    }
}

