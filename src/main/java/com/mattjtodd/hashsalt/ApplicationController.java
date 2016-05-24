package com.mattjtodd.hashsalt;

import javaslang.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class ApplicationController {

    private final TaskExecutor executor;

    private final Pbkdf2HashGenerator hashGenerator;

    @Autowired
    public ApplicationController(TaskExecutor executor, Pbkdf2HashGenerator hashGenerator) {
        this.executor = executor;
        this.hashGenerator = hashGenerator;
    }

    @RequestMapping(value="/hash", method= RequestMethod.POST)
    public DeferredResult<ResponseEntity<Pbkdf2Hash>> hash(@RequestBody HashRequest request) {
        DeferredResult<ResponseEntity<Pbkdf2Hash>> deferredResult = new DeferredResult<>(Long.MAX_VALUE);

        Future
            .fromTry(new ExecutorServiceAdapter(executor), hashGenerator.generate(request))
            .map(ResponseEntity::ok)
            .onSuccess(deferredResult::setResult)
            .onFailure(deferredResult::setErrorResult);

        return deferredResult;
    }
}
