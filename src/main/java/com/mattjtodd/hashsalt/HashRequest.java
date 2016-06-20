package com.mattjtodd.hashsalt;

import java.util.Arrays;

import static com.mattjtodd.hashsalt.ArrayCopyUtils.copyOf;

public class HashRequest {

    private final char[] characters;

    private final int iterations;

    private final String algorithm;

    private final int keyLength;

    private final int saltBytes;

    HashRequest() {
        this(new char[0], 0, "", 0, 0);
    }

    public HashRequest(char[] characters, int iterations, String algorithm, int keyLength, int saltBytes) {
        this.characters = copyOf(characters);
        this.iterations = iterations;
        this.algorithm = algorithm;
        this.keyLength = keyLength;
        this.saltBytes = saltBytes;
    }

    public char[] getCharacters() {
        return copyOf(characters);
    }

    public int getIterations() {
        return iterations;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public int getSaltBytes() {
        return saltBytes;
    }

    

    @Override
    public String toString() {
        return "HashRequest{" +
               "characters=" + Arrays.toString(characters) +
               ", iterations=" + iterations +
               ", algorithm='" + algorithm + '\'' +
               ", keyLength=" + keyLength +
               ", saltBytes=" + saltBytes +
               '}';
    }
}
