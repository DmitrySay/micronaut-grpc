package com.solbeg.demo.grpc.repository;

import com.solbeg.demo.grpc.model.UserEntity;


public interface UserRepository {
    UserEntity getOrSave(String email);
}
