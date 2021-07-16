package com.solbeg.demo.grpc;

import com.demo.grpc.DemoReply;
import com.demo.grpc.DemoRequest;
import com.demo.grpc.DemoServiceGrpc;
import io.grpc.ManagedChannel;
import io.micronaut.context.annotation.Factory;
import io.micronaut.grpc.annotation.GrpcChannel;

import javax.inject.Singleton;

@Factory
class Clients {
    @Singleton
    public DemoServiceGrpc.DemoServiceStub reactiveStub(@GrpcChannel("http://localhost:8080") ManagedChannel channel) {
        return DemoServiceGrpc.newStub(channel);
    }
}