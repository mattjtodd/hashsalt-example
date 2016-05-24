package com.mattjtodd.hashsalt;

import javaslang.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * http://nvlpubs.nist.gov/nistpubs/Legacy/SP/nistspecialpublication800-132.pdf
 */
@Component
public class Pbkdf2HashGenerator {

    private SaltSeller saltSeller;

    @Autowired
    private Pbkdf2HashGenerator(SaltSeller saltSeller) {
        this.saltSeller = saltSeller;
    }

    public Try<Pbkdf2Hash> generate(HashRequest request) {
        return Try.of(() -> hash(request));
    }

    private Pbkdf2Hash hash(HashRequest request)
        throws CharacterCodingException, InvalidKeySpecException, NoSuchAlgorithmException {
        int iterations = request.getIterations();
        byte[] salt = saltSeller.generate(request.getSaltBytes());

        PBEKeySpec spec = new PBEKeySpec(request.getCharacters(), salt, iterations, request.getKeyLength());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(request.getAlgorithm());

        char[] hash = toBase64UTF8CharArray(secretKeyFactory.generateSecret(spec).getEncoded());
        char[] encodedSalt = toBase64UTF8CharArray(salt);

        return new Pbkdf2Hash(secretKeyFactory.getAlgorithm(), encodedSalt, iterations, hash);
    }

    private static char[] toBase64UTF8CharArray(byte[] bytes) throws CharacterCodingException {
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        ByteBuffer buffer = ByteBuffer.wrap(encodedBytes);
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer charBuffer = decoder.decode(buffer);
        return charBuffer.array();
    }
}
