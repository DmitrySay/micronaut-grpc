package com.solbeg.demo.grpc.repository.impl;

import com.solbeg.demo.grpc.model.ChatEntity;
import com.solbeg.demo.grpc.repository.ChatRepository;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ChatRepositoryImpl implements ChatRepository {
    private Map<String, ChatEntity> chatsDatabase = new ConcurrentHashMap<>();

    public ChatRepositoryImpl() {
        chatsDatabase.put("channel_1", new ChatEntity(1L, "channel_1"));
        chatsDatabase.put("channel_2", new ChatEntity(2L, "channel_2"));
        chatsDatabase.put("channel_3", new ChatEntity(3L, "channel_3"));
    }

    @Override
    public Optional<ChatEntity> findChatByChannel(String channel) {
        return Optional.ofNullable(chatsDatabase.getOrDefault(channel, null));
    }
}
