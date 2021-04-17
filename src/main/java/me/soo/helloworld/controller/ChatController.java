package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.chat.ChatSendRequest;
import me.soo.helloworld.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @LoginRequired
    @MessageMapping("/chat")
    public void sendChat(@CurrentUser String sender, @Payload ChatSendRequest chatRequest) {
        chatService.sendChat(sender, chatRequest);
    }
}
