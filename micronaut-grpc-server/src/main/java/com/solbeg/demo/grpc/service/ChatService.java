package com.solbeg.demo.grpc.service;


import com.demo.grpc.ChatMessage;
import com.demo.grpc.ChatMessageFromServer;
import com.demo.grpc.ChatServiceGrpc;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;

@Slf4j
@RequiredArgsConstructor
@GrpcService
public class ChatService extends ChatServiceGrpc.ChatServiceImplBase {
    private static LinkedHashSet<StreamObserver<ChatMessageFromServer>> observers = new LinkedHashSet<>();

    @Override
    public StreamObserver<ChatMessage> chat(StreamObserver<ChatMessageFromServer> responseObserver) {
        observers.add(responseObserver);
        return new StreamObserver<ChatMessage>() {
            @Override
            public void onNext(ChatMessage value) {
                ChatMessageFromServer messageFromServer = ChatMessageFromServer
                        .newBuilder()
                        .setChatMessage(value)
                        .build();
                System.out.println("Server : " + messageFromServer + " from ");
                observers.stream().forEach(o -> o.onNext(messageFromServer));
            }
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                observers.remove(responseObserver);
            }
            @Override
            public void onCompleted() {
                observers.remove(responseObserver);
            }
        };
    }
}
