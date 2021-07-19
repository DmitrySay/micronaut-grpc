package com.solbeg.demo.grpc.repository.impl;

import com.solbeg.demo.grpc.model.UserEntity;
import com.solbeg.demo.grpc.repository.UserRepository;
import io.micronaut.context.annotation.Bean;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class UserRepositoryImpl implements UserRepository {
    private Map<String, UserEntity> usersDatabase = new ConcurrentHashMap<>();

    @Override
    public UserEntity getOrSave(String email) {
        UserEntity entity = usersDatabase.getOrDefault(email, null);
        if (Objects.isNull(entity)) {
            UserEntity userEntity = new UserEntity(new Random().nextLong(), email);
            usersDatabase.put(email, userEntity);
            return userEntity;
        } else {
            return entity;
        }
    }
}
