package com.solbeg.demo.grpc;


import com.demo.grpc.DemoReply;
import com.demo.grpc.DemoRequest;
import com.demo.grpc.DemoServiceGrpc;
import io.grpc.ManagedChannel;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.grpc.annotation.GrpcChannel;
import io.micronaut.runtime.event.annotation.EventListener;

import javax.inject.Inject;
import javax.inject.Singleton;


//@Singleton
public class GenerateMessage {
    private final ManagedChannel managedChannel;

    //@Inject
    public GenerateMessage(@GrpcChannel("demo-service") ManagedChannel managedChannel) {
        this.managedChannel = managedChannel;
    }

    //@EventListener
    public void generateMessage(StartupEvent event) {
        System.out.println("I am in generateMessage ");
        DemoServiceGrpc.DemoServiceBlockingStub stub = DemoServiceGrpc.newBlockingStub(managedChannel);

        final DemoRequest request = DemoRequest
                .newBuilder()
                .setName("Fred")
                .build();

        DemoReply reply = stub.send(request);
        System.out.println(reply);
    }
}
