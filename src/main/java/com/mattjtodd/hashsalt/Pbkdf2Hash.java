package com.mattjtodd.hashsalt;

import java.util.Arrays;

public class Pbkdf2Hash {
    private final char[] salt;
    private final char[] hash;
    private final int iterations;
    private final String generator;

    public Pbkdf2Hash(String generator, char[] salt, int iterations, char[] hash) {
        this.salt = copyOf(salt);
        this.hash = copyOf(hash);
        this.iterations = iterations;
        this.generator = generator;
    }

    public char[] getSalt() {
        return copyOf(salt);
    }

    public char[] getHash() {
        return copyOf(hash);
    }

    public int getIterations() {
        return iterations;
    }

    public String getGenerator() {
        return generator;
    }

    private static char[] copyOf(char[] chars) {
        return Arrays.copyOf(chars, chars.length);
    }
}
