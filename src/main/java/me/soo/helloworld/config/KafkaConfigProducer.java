package me.soo.helloworld.config;

import me.soo.helloworld.model.chat.ChatWrite;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfigProducer {

    @Value("${kafka.bootstrap.address}")
    private String bootStrapAddress;

    @Value("${kafka.producer.acks}")
    private String acksLevel;

    @Value("${kafka.producer.batch-size}")
    private String batchSize;

    @Value("${kafka.producer.linger-time}")
    private String lingerTime;

    @Bean
    public Map<String, Object> kafkaProducerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapAddress);
        props.put(ProducerConfig.ACKS_CONFIG, acksLevel);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerTime);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    @Bean
    public ProducerFactory<String, ChatWrite> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, ChatWrite> kafkaProducerTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }
}
