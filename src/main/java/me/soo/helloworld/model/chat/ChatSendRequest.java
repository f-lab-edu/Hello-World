package me.soo.helloworld.model.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ChatSendRequest {

    private final String recipient;

    private final String content;
}
