package com.solbeg.demo.grpc;

import com.demo.grpc.DemoServiceGrpc;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;

import javax.inject.Inject;

public class Application {
    @Inject
    DemoServiceGrpc DemoServiceGrpc;

    public static void main(String[] args) {
        ApplicationContext context = Micronaut.run(Application.class, args);

    }
}
