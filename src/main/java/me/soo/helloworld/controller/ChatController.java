package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.chat.ChatBox;
import me.soo.helloworld.model.chat.ChatSendRequest;
import me.soo.helloworld.service.ChatService;
import me.soo.helloworld.util.Pagination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    @Value("${max.page.size:30}")
    private int pageSize;

    private final ChatService chatService;

    @LoginRequired
    @MessageMapping("/chat")
    public void sendChat(@CurrentUser String sender, @Payload ChatSendRequest chatRequest) {
        chatService.sendChat(sender, chatRequest);
    }

    @LoginRequired
    @GetMapping("/chat-boxes")
    public List<ChatBox> getChatBoxes(@CurrentUser String userId,
                                      @RequestParam(required = false) Integer cursor) {
        return chatService.getChatBoxes(userId, Pagination.create(cursor, pageSize));
    }
}
