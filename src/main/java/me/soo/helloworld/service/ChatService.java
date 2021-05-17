package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.mapper.ChatMapper;
import me.soo.helloworld.model.chat.*;
import me.soo.helloworld.util.Pagination;
import me.soo.helloworld.util.validator.TargetUserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMapper chatMapper;

    private final UserService userService;

    private final KafkaProducerService kafkaProducerService;

    @Value("${kafka.topic.name}")
    private String kafkaTopic;

    @Transactional
    public void sendChat(String sender, ChatSendRequest chatRequest) {
        Integer chatBoxId = chatRequest.getChatBoxId();

        if (chatBoxId == null) {
            chatBoxId = fetchChatBoxId(sender, chatRequest.getRecipient());
        }

        ChatWrite chat = ChatWrite.create(sender, chatBoxId, chatRequest.getRecipient(),
                chatRequest.getContent(), chatRequest.getSentAt());

        kafkaProducerService.deliverChatsToKafka(kafkaTopic, chat);
        messagingTemplate.convertAndSendToUser(chat.getRecipient(), "/queue/messages",
                new ChatNotification(chat.getSender(), chat.getContent()));
    }

    @Transactional(readOnly = true)
    public List<ChatBox> getChatBoxes(String userId, Pagination pagination) {
        return chatMapper.getChatBoxes(userId, pagination);
    }

    @Transactional
    public List<Chat> getChats(int chatBoxId, String userId, Pagination pagination) {
        List<Chat> chats = chatMapper.getChats(chatBoxId, userId, pagination);
        markUnreadAsRead(userId, chats);
        return chats;
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

    private void markUnreadAsRead(String userId, List<Chat> chats) {
        List<Integer> unReadChatIds = chats.stream()
                .filter(chat -> "N".equals(chat.getHasRead()) && userId.equals(chat.getRecipient()))
                .map(Chat::getId)
                .collect(Collectors.toList());

        if (!unReadChatIds.isEmpty()) {
            chatMapper.updateToRead(unReadChatIds);
        }
    }
}
