package com.solbeg.demo.grpc.repository.impl;

import com.solbeg.demo.grpc.model.ChatEntity;
import com.solbeg.demo.grpc.repository.ChatRepository;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ChatRepositoryImpl implements ChatRepository {
    @Singleton
    private Map<String, ChatEntity> chatsDatabase = new ConcurrentHashMap<>();

    public ChatRepositoryImpl() {
        chatsDatabase.put("channel_1", new ChatEntity());
        chatsDatabase.put("channel_2", new ChatEntity());
        chatsDatabase.put("channel_3", new ChatEntity());
    }

    @Override
    public Optional<ChatEntity> findChatByChannel(String channel) {
        return Optional.ofNullable(chatsDatabase.getOrDefault(channel, null));
    }
}
