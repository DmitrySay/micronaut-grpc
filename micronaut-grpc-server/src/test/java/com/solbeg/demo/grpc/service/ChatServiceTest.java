package com.solbeg.demo.grpc.service;

import com.demo.grpc.ChatMessage;
import com.demo.grpc.ChatMessageFromServer;
import com.demo.grpc.ChatServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/*
 Notes: Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
class ChatServiceTest {

    private ChatServiceGrpc.ChatServiceStub asyncStub;

    @BeforeAll
    public void setup() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        this.asyncStub = ChatServiceGrpc.newStub(channel);
    }


    @Test
    void chatTest() throws InterruptedException {
        System.out.println("Beginning chat test");
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<ChatMessage> requestStreamObserver = asyncStub.chat(getClientStreamObserver(latch));

        //Generate 2 messages
        for (int i = 0; i < 2; i++) {
            ChatMessage request = ChatMessage
                    .newBuilder()
                    .setFrom(String.valueOf(ThreadLocalRandom.current().nextInt(1, 11)))
                    .setMessage(String.valueOf(ThreadLocalRandom.current().nextInt(1, 11)))
                    .build();
            requestStreamObserver.onNext(request);
        }
        requestStreamObserver.onCompleted();
        latch.await();
    }

    private StreamObserver<ChatMessageFromServer> getClientStreamObserver(CountDownLatch latch) {
        return new StreamObserver<>() {
            @Override
            public void onNext(ChatMessageFromServer value) {
                String logMsg = "Test client msg " + value;
                System.out.println(logMsg);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                System.out.println("onError.Disconnected");
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted.Disconnected");
                latch.countDown();
            }
        };
    }
}
