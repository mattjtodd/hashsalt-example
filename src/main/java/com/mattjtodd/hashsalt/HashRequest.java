package com.mattjtodd.hashsalt;

import java.util.Arrays;

public class HashRequest {

    private final char[] characters;

    private final int iterations;

    private final int saltBytes;

    private final String algorithm;

    private final int keyLength;

    HashRequest() {
        this(0, new char[0], 0, "", 0);
    }

    public HashRequest(int saltBytes, char[] characters, int iterations, String algorithm, int keyLength) {
        this.characters = Arrays.copyOf(characters, characters.length);
        this.iterations = iterations;
        this.saltBytes = saltBytes;
        this.algorithm = algorithm;
        this.keyLength = keyLength;
    }

    public int getIterations() {
        return iterations;
    }

    public char[] getCharacters() {
        return characters;
    }

    public int getSaltBytes() {
        return saltBytes;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getKeyLength() {
        return keyLength;
    }
}
