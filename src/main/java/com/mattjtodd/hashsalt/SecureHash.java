package com.mattjtodd.hashsalt;

import javaslang.Tuple2;
import javaslang.control.Try;
import org.apache.commons.lang3.RandomUtils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.apache.commons.lang3.ArrayUtils.addAll;

public class SecureHash {
    static Try<Tuple2<byte[], byte[]>> hash(String password, Supplier<byte[]> saltFunction, Function<byte[], byte[]> hashFunction) {
        return Try
            .of(() -> password.getBytes("UTF-8"))
            .map(hash -> new Tuple2<>(saltFunction.get(), hash))
            .map(value -> value.map((salt, hash) -> new Tuple2<>(salt, addAll(salt, hash))))
            .map(value -> value.map2(hashFunction));
    }

    public static void main(String[] args) throws Throwable {

        // use a SHA-256 message digest
        MessageDigest hashFunction = MessageDigest.getInstance("SHA-256");

        // User correct CSPRNG random number generator for salt generation
        SecureRandom secureRandom = new SecureRandom();

        // supplier function using the digest bytes length
        Supplier<byte[]> saltSeller = () -> {
            byte[] salt = new byte[hashFunction.getDigestLength()];
            secureRandom.nextBytes(salt);
            return salt;
        };

        Tuple2<byte[], byte[]> password = hash("password", saltSeller, hashFunction::digest)
            .getOrElseThrow(thrown -> new RuntimeException("Error creating secure hash", thrown.getCause()));

        // The salt
        System.out.println(Base64.getEncoder().encodeToString(password._1));

        // The hashed salt / SHA-256 combination
        System.out.println(Base64.getEncoder().encodeToString(password._2));
    }
}
