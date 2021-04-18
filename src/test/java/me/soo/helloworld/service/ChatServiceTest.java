package me.soo.helloworld.service;

import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.ChatMapper;
import me.soo.helloworld.model.chat.ChatData;
import me.soo.helloworld.model.chat.ChatNotification;
import me.soo.helloworld.model.chat.ChatSendRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    public static final int CHAT_BOX_ID = 1;

    private final String sender = "Soo";

    private final String recipient = "NotSoo";

    private final String content = "Test";

    @InjectMocks
    ChatService chatService;

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @Mock
    ChatMapper chatMapper;

    @Mock
    UserService userService;

    ChatSendRequest requestIncludingChatBoxId;

    ChatSendRequest requestWithoutChatBoxId;

    ChatSendRequest requestWithoutChatBoxIdToSelf;

    ChatData chatData;

    @BeforeEach
    public void createChatRequest() {
        requestIncludingChatBoxId = ChatSendRequest.builder()
                .chatBoxId(CHAT_BOX_ID)
                .recipient(recipient)
                .content(content)
                .build();

        requestWithoutChatBoxId = ChatSendRequest.builder()
                .recipient(recipient)
                .content(content)
                .build();

        requestWithoutChatBoxIdToSelf = ChatSendRequest.builder()
                .recipient(sender)
                .content(content)
                .build();
    }

    @BeforeEach
    public void createChatDataAndNotification() {
        chatData = ChatData.create(sender, CHAT_BOX_ID, recipient, content);
    }

    @Test
    @DisplayName("Chat 관련 데이터와 ChatBox 아이디가 함께 전달되는 경우 별도의 처리과정 없이 해당 ChatBox 아이디를 이용해 메시지를 저장하고 전달하는데 성공합니다.")
    public void sendChatSuccessWhenChatBoxIdComesTogether() {
        doNothing().when(chatMapper).insertChat(refEq((chatData)));
        doNothing().when(messagingTemplate).convertAndSendToUser(anyString(), anyString(), any(ChatNotification.class));

        chatService.sendChat(sender, requestIncludingChatBoxId);

        verify(chatMapper, never()).isChatBoxExist(sender, recipient);
        verify(userService, never()).isUserActivated(recipient);
        verify(chatMapper, never()).insertChatBox(sender, recipient);
        verify(chatMapper, never()).getChatBoxId(sender, recipient);
        verify(chatMapper, times(1)).insertChat(refEq(chatData));
        verify(messagingTemplate, times(1)).convertAndSendToUser(anyString(), anyString(), any(ChatNotification.class));
    }

    @Test
    @DisplayName("ChatBox 아이디 값 없이 Chat 관련 데이터만 전달되는 경우 ChatBox 의 존재 유무를 확인하고 없으면 ChatBox 를 생성하는 과정을 거치는데" +
            " 기존 ChatBox 가 존재하지 않고 sender 와 recipient 가 동일 사용자인 경우 InvalidRequestException 이 발생하며 해당 메시지를 전달하는데 실패합니다.")
    public void sendChatFailWithoutChatBoxIdWhenTryingToSendToSelf() {
        assertThrows(InvalidRequestException.class, () -> {
            chatService.sendChat(sender, requestWithoutChatBoxIdToSelf);
        });

        verify(chatMapper, times(1)).isChatBoxExist(sender, sender);
        verify(userService, never()).isUserActivated(recipient);
        verify(chatMapper, never()).insertChatBox(sender, recipient);
        verify(chatMapper, never()).getChatBoxId(sender, recipient);
        verify(chatMapper, never()).insertChat(refEq(chatData));
        verify(messagingTemplate, never()).convertAndSendToUser(anyString(), anyString(), any(ChatNotification.class));
    }

    @Test
    @DisplayName("ChatBox 아이디 값 없이 Chat 관련 데이터만 전달되는 경우 ChatBox 의 존재 유무를 확인하고 없으면 ChatBox 를 생성하는 과정을 거치는데" +
            " 이때 recipient 가 존재하지 않는 사용자인 경우 InvalidRequestException 이 발생하며 해당 메시지를 전달하는데 실패합니다.")
    public void sendChatFailWithoutChatBoxIdWhenTryingToSendToNonExistingUser() {
        when(userService.isUserActivated(recipient)).thenReturn(false);

        assertThrows(InvalidRequestException.class, () -> {
            chatService.sendChat(sender, requestWithoutChatBoxId);
        });

        verify(chatMapper, times(1)).isChatBoxExist(sender, recipient);
        verify(userService, times(1)).isUserActivated(recipient);
        verify(chatMapper, never()).insertChatBox(sender, recipient);
        verify(chatMapper, never()).getChatBoxId(sender, recipient);
        verify(chatMapper, never()).insertChat(refEq(chatData));
        verify(messagingTemplate, never()).convertAndSendToUser(anyString(), anyString(), any(ChatNotification.class));
    }

    @Test
    @DisplayName("ChatBox 아이디 값 없이 Chat 관련 데이터만 전달되는 경우 ChatBox 의 존재 유무를 확인하고 없으면 상호 간의 새로운 ChatBox 를 생성한 뒤 " +
            " 해당 ChatBox 의 아이디를 얻어와 해당 메시지를 전달합니다.")
    public void sendChatSuccessWithoutChatBoxIdByCreatingNewChatBoxWhenNoChatBoxExistsBetweenTwo() {
        when(chatMapper.isChatBoxExist(sender, recipient)).thenReturn(false);
        when(userService.isUserActivated(recipient)).thenReturn(true);
        doNothing().when(chatMapper).insertChatBox(sender, recipient);
        when(chatMapper.getChatBoxId(sender, recipient)).thenReturn(CHAT_BOX_ID);
        doNothing().when(chatMapper).insertChat(refEq((chatData)));
        doNothing().when(messagingTemplate).convertAndSendToUser(anyString(), anyString(), any(ChatNotification.class));

        chatService.sendChat(sender, requestWithoutChatBoxId);

        verify(chatMapper, times(1)).isChatBoxExist(sender, recipient);
        verify(userService, times(1)).isUserActivated(recipient);
        verify(chatMapper, times(1)).insertChatBox(sender, recipient);
        verify(chatMapper, times(1)).getChatBoxId(sender, recipient);
        verify(chatMapper, times(1)).insertChat(refEq(chatData));
        verify(messagingTemplate, times(1)).convertAndSendToUser(anyString(), anyString(), any(ChatNotification.class));
    }

    @Test
    @DisplayName("ChatBox 아이디 값 없이 Chat 관련 데이터만 전달되는 경우 ChatBox 의 존재 유무를 확인하고 있으면 새로운 ChatBox 의 생성과정 없이 기존의 " +
            " ChatBox 의 아이디를 얻어와 해당 메시지를 전달합니다.")
    public void sendChatSuccessWithoutChatBoxIdByFetchingChatBoxIdWhenOneChatBoxAlreadyExistsBetweenTwo() {
        when(chatMapper.isChatBoxExist(sender, recipient)).thenReturn(true);
        when(chatMapper.getChatBoxId(sender, recipient)).thenReturn(CHAT_BOX_ID);
        doNothing().when(chatMapper).insertChat(refEq((chatData)));
        doNothing().when(messagingTemplate).convertAndSendToUser(anyString(), anyString(), any(ChatNotification.class));

        chatService.sendChat(sender, requestWithoutChatBoxId);

        verify(chatMapper, times(1)).isChatBoxExist(sender, recipient);
        verify(userService, never()).isUserActivated(recipient);
        verify(chatMapper, never()).insertChatBox(sender, recipient);
        verify(chatMapper, times(1)).getChatBoxId(sender, recipient);
        verify(chatMapper, times(1)).insertChat(refEq(chatData));
        verify(messagingTemplate, times(1)).convertAndSendToUser(anyString(), anyString(), any(ChatNotification.class));
    }
}
