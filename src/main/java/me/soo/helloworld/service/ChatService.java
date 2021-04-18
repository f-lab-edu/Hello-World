package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.mapper.ChatMapper;
import me.soo.helloworld.model.chat.ChatData;
import me.soo.helloworld.model.chat.ChatNotification;
import me.soo.helloworld.model.chat.ChatSendRequest;
import me.soo.helloworld.util.validator.TargetUserValidator;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMapper chatMapper;

    private final UserService userService;

    @Transactional
    public void sendChat(String sender, ChatSendRequest chatRequest) {
        Integer chatBoxId = chatRequest.getChatBoxId();

        if (chatBoxId == null) {
            chatBoxId = fetchChatBoxId(sender, chatRequest.getRecipient());
        }

        ChatData chatData = ChatData.create(sender, chatBoxId, chatRequest.getRecipient(), chatRequest.getContent());
        chatMapper.insertChat(chatData);
        messagingTemplate.convertAndSendToUser(chatRequest.getRecipient(), "/queue/messages",
                new ChatNotification(chatRequest.getRecipient(), chatRequest.getContent()));
    }

    private int fetchChatBoxId(String sender, String recipient) {
        createIfNotExist(sender, recipient);
        return chatMapper.getChatBoxId(sender, recipient);
    }

    private void createIfNotExist(String sender, String recipient) {
        boolean isChatBoxExist = chatMapper.isChatBoxExist(sender, recipient);

        if (!isChatBoxExist) {
            TargetUserValidator.targetNotSelf(sender, recipient);
            TargetUserValidator.targetExistence(userService.isUserActivated(recipient));
            chatMapper.insertChatBox(sender, recipient);
        }
    }
}
