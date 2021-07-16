package com.solbeg.demo.grpc;

import com.demo.grpc.DemoServiceGrpc;
import io.grpc.ManagedChannel;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.grpc.annotation.GrpcChannel;
import io.micronaut.grpc.server.GrpcServerChannel;

@Factory
class Clients {
    @Bean
    DemoServiceGrpc.DemoServiceBlockingStub blockingStub(@GrpcChannel(GrpcServerChannel.NAME) ManagedChannel channel) {
        return DemoServiceGrpc.newBlockingStub(channel);
    }
}

