package com.solbeg.demo.grpc.service;

import com.demo.grpc.DemoReply;
import com.demo.grpc.DemoRequest;
import com.demo.grpc.DemoServiceGrpc;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;

@GrpcService
public class DemoService extends DemoServiceGrpc.DemoServiceImplBase {

    @Override
    public void send(DemoRequest request, StreamObserver<DemoReply> responseObserver) {
        final String name = request.getName();
        final String message = "Hello World and Hello " + name;
        DemoReply reply = DemoReply.newBuilder().setMessage(message).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
