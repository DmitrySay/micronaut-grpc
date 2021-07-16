package com.solbeg.demo.grpc;

import com.demo.grpc.DemoRequest;
import com.demo.grpc.DemoServiceGrpc;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class DemoEndpointTest {

    @Inject
    DemoServiceGrpc.DemoServiceBlockingStub blockingStub;

    @Test
    void testHelloWorld() {
        final DemoRequest request = DemoRequest
                .newBuilder()
                .setName("Fred")
                .build();

        String responseMessage = blockingStub.send(request).getMessage();
        assertEquals("Hello World and Hello Fred", responseMessage);
    }

}
