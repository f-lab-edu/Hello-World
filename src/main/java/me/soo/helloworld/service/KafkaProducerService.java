package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.model.chat.ChatWrite;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, ChatWrite> kafkaProducerTemplate;

    public void deliverChatsToKafka(String topic, ChatWrite chat) {
        ListenableFuture<SendResult<String, ChatWrite>> sendResult = kafkaProducerTemplate.send(topic, chat);
        sendResult.addCallback(new ListenableFutureCallback<SendResult<String, ChatWrite>>() {
            @Override
            public void onSuccess(SendResult<String, ChatWrite> result) {
                log.info("successfully sent a message {} with offset {}", chat, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("fail to send a message {} because of {}", chat, ex.getMessage());
                throw new MessagingException("fail to send a message", ex);
            }
        });
    }
}
