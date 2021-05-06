package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.mapper.ChatMapper;
import me.soo.helloworld.model.chat.ChatWrite;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ChatMapper chatMapper;

    @KafkaListener(
            groupId = "${kafka.consumer.group-id}",
            topics = "${kafka.topic.name}"
    )
    public void saveChats(List<ChatWrite> chats) {
        chatMapper.insertChats(chats);
    }
}
