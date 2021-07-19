package com.solbeg.demo.grpc.controller;

import com.solbeg.demo.grpc.service.ChatClientService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
@Controller("/api/chat")
public class ChatController {
    private final ChatClientService chatClientService;


    @Get("/{name}/{channel}")
    public String joinChat(@PathVariable String name, @PathVariable String channel) {
        return chatClientService.joinChat(name, channel);
    }

    @Get("/{channel}/messages")
    public List<String> joinChat(@PathVariable String channel) {
        return chatClientService.getAllMessages(channel);
    }

    @Post("/{channel}")
    public String sendMessage(@PathVariable String channel, @Body String message) {
        return chatClientService.sendMessage(message, channel);
    }
}
