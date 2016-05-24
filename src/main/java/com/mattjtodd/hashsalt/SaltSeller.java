package com.mattjtodd.hashsalt;

public interface SaltSeller {
    /**
     * Generates a random salt.
     *
     * @return the salt
     */
    byte[] generate(int length);
}
