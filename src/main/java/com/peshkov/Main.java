package com.peshkov;

import com.peshkov.Matrices.*;

public class Main {
    public static void main(String[] args) {
        var m = new Matrix(3, 2, Float::valueOf);
        System.out.println(m.get(2, 1));
        System.out.println(m.setRow(1, new Matrix(1, 2, () -> 3f)));
    }
}

