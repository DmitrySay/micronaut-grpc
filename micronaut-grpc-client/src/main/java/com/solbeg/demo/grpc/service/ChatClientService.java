package com.solbeg.demo.grpc.service;

import com.demo.grpc.ChatMessagesRequest;
import com.demo.grpc.ChatMessagesResponse;
import com.demo.grpc.ChatServiceGrpc;
import com.demo.grpc.JoinRequest;
import com.demo.grpc.JoinResponse;
import com.demo.grpc.SendRequest;
import com.demo.grpc.SendResponse;
import io.grpc.Channel;
import io.micronaut.grpc.annotation.GrpcChannel;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class ChatClientService {
    private Channel channel;
    private final ChatServiceGrpc.ChatServiceBlockingStub blockingStub;

    @Inject
    public ChatClientService(@GrpcChannel("demo-service") Channel channel) {
        this.channel = channel;
        blockingStub = ChatServiceGrpc.newBlockingStub(channel);
    }

    public String joinChat(String name, String channel) {
        JoinRequest request = JoinRequest.newBuilder()
                .setUsername(name)
                .setChannel(channel)
                .build();
        JoinResponse response = blockingStub.join(request);
        return response.toString();
    }

    public List<String> getAllMessages(String channel) {
        ChatMessagesRequest request = ChatMessagesRequest.newBuilder()
                .setChannel(channel)
                .build();

        ChatMessagesResponse chatMessagesResponse = blockingStub.getAllMessages(request);
        return chatMessagesResponse.getChatMessageList();

    }

    public String sendMessage(String message, String channel) {
        SendRequest request = SendRequest.newBuilder()
                .setMessage(message)
                .setChannel(channel)
                .build();
        SendResponse response = blockingStub.send(request);
        return response.toString();
    }

}
