package com.mattjtodd.hashsalt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.function.Supplier;

@Component
public class SecureRandomSaltSeller implements SaltSeller {

    private final Supplier<SecureRandom> randomSource;

    @Autowired
    public SecureRandomSaltSeller(Supplier<SecureRandom> randomSource) {
        this.randomSource = randomSource;
    }

    @Override
    public byte[] generate(int length) {
        SecureRandom secureRandom = randomSource.get();
        byte[] salt = new byte[length];
        secureRandom.nextBytes(salt);
        return salt;
    }
}
