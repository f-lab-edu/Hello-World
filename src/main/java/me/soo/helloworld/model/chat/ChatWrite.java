package me.soo.helloworld.model.chat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatWrite {

    private int chatBoxId;

    private String recipient;

    private String sender;

    private String content;

    private LocalDateTime sentAt;

    public static ChatWrite create(String sender, int chatBoxId,
                                   String recipient, String content, LocalDateTime sentAt) {

        return ChatWrite.builder()
                .chatBoxId(chatBoxId)
                .recipient(recipient)
                .sender(sender)
                .content(content)
                .sentAt(sentAt)
                .build();
    }
}
