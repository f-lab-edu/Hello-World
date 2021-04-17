package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.mapper.ChatMapper;
import me.soo.helloworld.model.chat.ChatNotification;
import me.soo.helloworld.model.chat.ChatSendRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMapper chatMapper;

    @Transactional
    public void sendChat(String sender, ChatSendRequest chatRequest) {
        chatMapper.insertChat(sender, chatRequest);
        notifyChat(chatRequest);
    }

    private void notifyChat(ChatSendRequest chatRequest) {
        messagingTemplate.convertAndSendToUser(chatRequest.getRecipient(), "/queue/messages",
                new ChatNotification(chatRequest.getRecipient(), chatRequest.getContent()));
    }
}
