package me.soo.helloworld.config;

import me.soo.helloworld.model.chat.ChatWrite;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.RecoveringBatchErrorHandler;
import org.springframework.kafka.listener.RetryingBatchErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfigConsumer {

    @Value("${kafka.bootstrap.address}")
    private String bootStrapAddress;

    @Value("${kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.consumer.enable-auto-commit}")
    private String isAutoCommit;

    @Value("${kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${kafka.consumer.fetch-min-size}")
    private String minFetchSize;

    @Value("${kafka.consumer.max-poll-records}")
    private String maxPollRecords;

    @Value("${kafka.consumer.fetch-max-wait}")
    private String maxFetchWait;

    @Bean
    public Map<String, Object> kafkaConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, isAutoCommit);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, minFetchSize);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, maxFetchWait);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return props;
    }

    @Bean
    public ConsumerFactory<String, ChatWrite> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfig(),
                new StringDeserializer(), new JsonDeserializer<>(ChatWrite.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ChatWrite> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatWrite> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory());
        factory.setBatchListener(true);
        factory.setBatchErrorHandler(new RecoveringBatchErrorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        return factory;
    }
}
