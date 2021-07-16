package com.solbeg.demo.grpc;

import com.demo.grpc.DemoReply;
import com.demo.grpc.DemoRequest;
import com.demo.grpc.DemoServiceGrpc;
import io.grpc.stub.StreamObserver;

import javax.inject.Singleton;

@Singleton
public class DemoEndpoint extends DemoServiceGrpc.DemoServiceImplBase {

    @Override
    public void send(DemoRequest request, StreamObserver<DemoReply> responseObserver) {
        final String name = request.getName();
        final String message = "Hello World and Hello " + name;
        DemoReply reply = DemoReply.newBuilder().setMessage(message).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
