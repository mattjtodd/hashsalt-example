package com.mattjtodd.hashsalt;

import java.util.Arrays;

import static com.mattjtodd.hashsalt.ArrayCopyUtils.copyOf;

public class HashResponse {

    private final byte[] salt;

    private final byte[] hash;

    HashResponse() {
        this(new byte[0], new byte[0]);
    }

    public HashResponse(byte[] salt, byte[] hash) {
        this.salt = copyOf(salt);
        this.hash = copyOf(hash);
    }

    public byte[] getSalt() {
        return copyOf(salt);
    }

    public byte[] getHash() {
        return copyOf(hash);
    }

    @Override
    public String toString() {
        return "HashResponse{" +
               "salt=" + Arrays.toString(salt) +
               ", hash=" + Arrays.toString(hash) +
               '}';
    }
}
