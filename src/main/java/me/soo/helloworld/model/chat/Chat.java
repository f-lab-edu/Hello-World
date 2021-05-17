package me.soo.helloworld.model.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Chat {

    private final int id;

    private final String recipient;

    private final String sender;

    private final String content;

    private final String hasRead;

    private final LocalDateTime sentAt;
}
