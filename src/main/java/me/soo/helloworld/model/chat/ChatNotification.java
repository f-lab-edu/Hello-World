package me.soo.helloworld.model.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatNotification {

    private final String sender;

    private final String content;
}
