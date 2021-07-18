package com.solbeg.demo.grpc.service;

import com.demo.grpc.DemoReply;
import com.demo.grpc.DemoRequest;
import com.demo.grpc.DemoServiceGrpc;
import io.grpc.ManagedChannel;
import io.micronaut.grpc.annotation.GrpcChannel;

import javax.inject.Singleton;

@Singleton
public class DemoClientService {
    private final ManagedChannel managedChannel;
    private final DemoServiceGrpc.DemoServiceBlockingStub stubDemo;

    public DemoClientService(@GrpcChannel("demo-service") ManagedChannel managedChannel) {
        this.managedChannel = managedChannel;
        stubDemo = DemoServiceGrpc.newBlockingStub(managedChannel);
    }

    public String generateDemoMessage(String name) {
        final DemoRequest request = DemoRequest.newBuilder().setName(name).build();
        DemoReply reply = stubDemo.send(request);
        return reply.getMessage();
    }
}
