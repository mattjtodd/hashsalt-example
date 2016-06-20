package com.mattjtodd.hashsalt;

import javaslang.Function1;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.*;
import java.util.function.IntFunction;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationControllerTest {

    @Mock
    private IntFunction<byte[]> saltSeller;

    private ApplicationController controller;

    private ExecutorService executorService;

    @Before
    public void setUp() {
        executorService = Executors.newSingleThreadExecutor();
        controller = new ApplicationController(executorService, saltSeller);
    }

    @Test
    public void hashConsistent()
        throws UnsupportedEncodingException, InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] bites = "bites".getBytes("UTF-8");
        when(saltSeller.apply(64)).thenReturn(bites);

        HashRequest hashRequest = new HashRequest("hashingtest".toCharArray(), 10, "PBKDF2WithHmacSHA512", 256, 64);

        DeferredResult<ResponseEntity<HashResponse>> result = controller.hash(hashRequest);

        ResponseEntity<HashResponse> entity = awaitResult(result);

        System.out.println(entity.getBody());
    }

    private ResponseEntity<HashResponse> awaitResult(DeferredResult<ResponseEntity<HashResponse>> result) throws InterruptedException {
        boolean complete = result.isSetOrExpired();
        while(!complete) {
            Thread.sleep(10);
            complete = result.isSetOrExpired();
        }
        return (ResponseEntity<HashResponse>) result.getResult();
    }

    @After
    public void tearDown() {
        executorService.shutdown();
    }
}
