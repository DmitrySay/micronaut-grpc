package com.solbeg.demo.grpc.service;


import com.demo.grpc.ChatMessage;
import com.demo.grpc.ChatMessageFromServer;
import com.demo.grpc.ChatServiceGrpc;
import com.solbeg.demo.grpc.dto.Message;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class ChatClientService {
    private ManagedChannel channel;
    private final ChatServiceGrpc.ChatServiceStub stub;
    @Getter
    private List<Message> messages = new ArrayList<>();

    @Inject
    public ChatClientService(@GrpcChannel("demo-service") ManagedChannel channel) {
        this.channel = channel;
        stub = ChatServiceGrpc.newStub(channel);
    }

    public void chat(Message message) {
        StreamObserver<ChatMessage> observer = stub.chat(new StreamObserver<ChatMessageFromServer>() {
            @Override
            public void onNext(ChatMessageFromServer value) {
                String from = value.getChatMessage().getFrom();
                String messageFromServer = value.getChatMessage().getMessage();
                System.out.println("Client : " + messageFromServer + " from " + from);
                messages.add(new Message(from, messageFromServer));

            }
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("exit");
            }
        });

        ChatMessage chatMessage = ChatMessage.newBuilder()
                .setFrom(message.getFrom())
                .setMessage(message.getMessage()).build();

          observer.onNext(chatMessage);
    }
}
