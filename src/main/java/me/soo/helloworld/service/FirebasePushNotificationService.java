package me.soo.helloworld.service;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.exception.FCMUninitializedException;
import me.soo.helloworld.mapper.PushNotificationMapper;
import me.soo.helloworld.model.notification.PushNotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebasePushNotificationService implements PushNotificationService {

    @Value("${fcm.account.path}")
    private String accountPath;

    private final PushNotificationMapper pushNotificationMapper;

    @PostConstruct
    public void init() {
        try (InputStream serviceAccount = Files.newInputStream(Paths.get(accountPath))) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase Cloud Messaging 서비스를 성공적으로 초기화하였습니다.");
            }
        } catch (IOException e) {
            throw new FCMUninitializedException("Firebase Cloud Messaging 서비스의 초기화에 실패하였습니다.", e);
        }
    }

    /*
        사용자 로그인 시 토큰이 같이 전송되었다면 토큰을 등록합니다.
     */
    @Override
    public void registerToken(String userId, String token) {
        pushNotificationMapper.upsertToken(userId, token);
    }

    @Override
    public String getToken(String userId) {
        return pushNotificationMapper.getToken(userId);
    }

    @Override
    public void sendPushNotification(PushNotificationRequest pushNotificationRequest) {
        String token = getToken(pushNotificationRequest.getTargetId());

        if (token == null) {
            return;
        }

        Message pushMessage = writePushMessage(pushNotificationRequest, token);
        FirebaseMessaging.getInstance().sendAsync(pushMessage);
    }

    private Message writePushMessage(PushNotificationRequest pushNotificationRequest, String token) {
        return Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(WebpushNotification.builder()
                                .setTitle(pushNotificationRequest.getTitle())
                                .setBody(pushNotificationRequest.getMessage())
                                .build())
                        .build())
                .setToken(token)
                .build();
    }
}
