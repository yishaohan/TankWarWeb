package com.ysh;

import java.util.Random;

public enum Direction {
    L, R, U, D/*, LU, RU, LD, RD*/;

    private static Random r = new Random();

    public static Direction randomDirection () {
        return values()[r.nextInt(values().length)];
    }
}
