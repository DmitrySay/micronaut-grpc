package com.solbeg.demo.grpc.controller;


import com.solbeg.demo.grpc.dto.Message;
import com.solbeg.demo.grpc.service.ChatClientService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Controller("/chat")
public class ChatClientController {
    private final ChatClientService chatClientService;

    @Post
    public void chat(@Body Message message) {
        chatClientService.chat(message);
    }

    @Get("/messages")
    public List<Message> getAll() {
        return chatClientService.getMessages();
    }
}
