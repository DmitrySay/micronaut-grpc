package com.solbeg.demo.grpc.service;


import com.demo.grpc.ChatMessagesRequest;
import com.demo.grpc.ChatMessagesResponse;
import com.demo.grpc.ChatServiceGrpc;
import com.demo.grpc.JoinRequest;
import com.demo.grpc.JoinResponse;
import com.demo.grpc.SendRequest;
import com.demo.grpc.SendResponse;
import com.solbeg.demo.grpc.model.ChatEntity;
import com.solbeg.demo.grpc.model.UserEntity;
import com.solbeg.demo.grpc.repository.ChatRepository;
import com.solbeg.demo.grpc.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@GrpcService
public class ChatService extends ChatServiceGrpc.ChatServiceImplBase {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public void join(JoinRequest request, StreamObserver<JoinResponse> responseObserver) {
        String channel = request.getChannel();
        String username = request.getUsername();

        Optional<ChatEntity> chatByChannel = chatRepository.findChatByChannel(channel);
        UserEntity user = userRepository.getOrSave(username);

        JoinResponse response;
        if (chatByChannel.isEmpty()) {
            response = JoinResponse.newBuilder()
                    .setStatus("ERROR")
                    .setMessage("channel " + channel + " does not exist ").build();

        } else if (chatByChannel.get().getPeople().contains(user)) {
            response = JoinResponse.newBuilder()
                    .setStatus("ERROR")
                    .setMessage("user " + username + " already joined " + channel).build();

        } else {
            ChatEntity chat = chatByChannel.get();
            chat.getPeople().add(user);
            chat.getMessages().add(username + " has joined " + channel);
            response = JoinResponse.newBuilder()
                    .setStatus("OK")
                    .setMessage(username + " has joined " + channel)
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void send(SendRequest request, StreamObserver<SendResponse> responseObserver) {
        String channel = request.getChannel();
        String message = request.getMessage();
        Optional<ChatEntity> chatByChannel = chatRepository.findChatByChannel(channel);
        SendResponse response;
        if (chatByChannel.isEmpty()) {
            response = SendResponse.newBuilder()
                    .setStatus("ERROR")
                    .setMessage("No " + channel).build();
        } else {
            ChatEntity chat = chatByChannel.get();
            chat.getMessages().add(message);
            response = SendResponse.newBuilder()
                    .setStatus("OK")
                    .setMessage(message)
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllMessages(ChatMessagesRequest request, StreamObserver<ChatMessagesResponse> responseObserver) {
        String channel = request.getChannel();
        Optional<ChatEntity> chatByChannel = chatRepository.findChatByChannel(channel);
        ChatMessagesResponse response;
        if (chatByChannel.isEmpty()) {
            response = ChatMessagesResponse.newBuilder()
                    .setStatus("ERROR")
                    .setChatMessage(0, "No " + channel).build();
            responseObserver.onNext(response);
        } else {
            ChatEntity chat = chatByChannel.get();
            response = ChatMessagesResponse.newBuilder()
                    .setStatus("OK")
                    .setChannel(channel)
                    .addAllChatMessage(chat.getMessages()).build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }
}
