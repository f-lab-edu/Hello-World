package me.soo.helloworld.model.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PushNotificationRequest {

    private final String targetId;

    private final String title;

    private final String message;

    public static PushNotificationRequest create(String targetId, String title, String message) {

        return PushNotificationRequest.builder()
                                    .targetId(targetId)
                                    .title(title)
                                    .message(message)
                                    .build();
    }
}
