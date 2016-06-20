package com.mattjtodd.hashsalt;

import java.util.Arrays;

class ArrayCopyUtils {

    private ArrayCopyUtils() {
        throw new AssertionError();
    }

    static byte[] copyOf(byte[] array) {
        return Arrays.copyOf(array, array.length);
    }

    static char[] copyOf(char[] array) {
        return Arrays.copyOf(array, array.length);
    }
}
