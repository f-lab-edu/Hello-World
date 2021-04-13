package me.soo.helloworld.service;

import me.soo.helloworld.model.notification.PushNotificationRequest;

public interface PushNotificationService {

    public void registerToken(String userId, String token);

    public String getToken(String userId);

    public void destroyToken(String userId);

    public void sendPushNotification(PushNotificationRequest notificationRequest);
}
