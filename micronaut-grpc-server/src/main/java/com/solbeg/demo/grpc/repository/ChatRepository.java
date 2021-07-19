package com.solbeg.demo.grpc.repository;

import com.solbeg.demo.grpc.model.ChatEntity;

import java.util.Optional;


public interface ChatRepository {
    Optional<ChatEntity> findChatByChannel(String channel);
}
