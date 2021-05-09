package me.soo.helloworld.model.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class ChatSendRequest {

    private final Integer chatBoxId;

    private final String recipient;

    private final String content;

    private final LocalDateTime sentAt;
}
