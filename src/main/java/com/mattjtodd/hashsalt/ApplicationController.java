package com.mattjtodd.hashsalt;

import javaslang.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ExecutorService;
import java.util.function.IntFunction;
import java.util.logging.Logger;

/**
 * Spring MVC application controller.
 */
@RestController
public class ApplicationController {

    /**
     * The executor service to use for executing the encoding PBKDF2 jobs.
     */
    private final ExecutorService executor;

    /**
     * The source of random salt bytes.
     */
    private final IntFunction<byte[]> randomSource;

    @Autowired
    public ApplicationController(ExecutorService executor, IntFunction<byte[]> randomSource) {
        this.executor = executor;
        this.randomSource = randomSource;
    }

    /**
     * Encodes the request into the response.  This is performed asynchronously using a {@link DeferredResult}.
     *
     * @param request the hashing request
     * @return the encoded response
     */
    @RequestMapping(value = "/hash", method = RequestMethod.POST)
    public DeferredResult<ResponseEntity<HashResponse>> hash(@RequestBody HashRequest request) {
        DeferredResult<ResponseEntity<HashResponse>> deferredResult = new DeferredResult<>(Long.MAX_VALUE);

        Future
            .of(executor, () -> encode(request, randomSource))
            .map(ResponseEntity::ok)
            .onSuccess(deferredResult::setResult)
            .onFailure(thrown -> deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)));

        return deferredResult;
    }

    /**
     * Creates a Pbkdf2 has for the supplied request.
     *
     * @param params the request containing the paraemeters
     * @return the requested hash
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private static HashResponse encode(HashRequest params, IntFunction<byte[]> randomSource) throws InvalidKeySpecException,
                                                                         NoSuchAlgorithmException {
        Logger.getAnonymousLogger().info("Encoding: " + params);
        int iterations = params.getIterations();
        byte[] salt = randomSource.apply(params.getSaltBytes());

        PBEKeySpec spec = new PBEKeySpec(params.getCharacters(), salt, iterations, params.getKeyLength());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(params.getAlgorithm());

        byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();

        return new HashResponse(salt, hash);
    }
}
