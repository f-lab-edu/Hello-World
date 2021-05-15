package me.soo.helloworld.model.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ChatBox {

    private final int id;

    private final int chatBoxId;

    private final String partner;

    private final String content;

    private final LocalDateTime sentAt;
}
