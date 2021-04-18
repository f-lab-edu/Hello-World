package me.soo.helloworld.model.chat;

import lombok.*;

@Getter
@Builder
@RequiredArgsConstructor
@ToString
public class ChatData {

    private final int chatBoxId;

    private final String recipient;

    private final String sender;

    private final String content;

    public static ChatData create(String sender, int chatBoxId, String recipient, String content) {

        return ChatData.builder()
                .chatBoxId(chatBoxId)
                .recipient(recipient)
                .sender(sender)
                .content(content)
                .build();
    }

}
