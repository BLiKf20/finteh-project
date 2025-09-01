package ru.finteh.creditapplicationservice.kafka.configuration;

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

/**
 * Конфигурационный класс для Kafka, который настраивает
 * Kafka Producer — для отправки сообщений в Kafka,
 * KafkaTemplate — удобный Spring-обёртку для отправки сообщений.
 * Он не отправляет сообщения сам, а настраивает инфраструктуру, через которую другие компоненты будут отправлять события.
 *
 * 1. Приложение запускается
 * 2. Spring видит @Configuration → создаёт бины
 * 3. Создаётся producerFactory с настройками из application.yml
 * 4. Создаётся kafkaTemplate, использующий этот factory
 * 5. KafkaTemplate:
 *    - Сериализует ключ в String
 *    - Сериализует event в JSON
 *    - Отправляет в Kafka через Producer
 */

@Configuration
public class KafkaConfiguration {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        // Адрес Kafka-кластера
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Как сериализовать ключ сообщения
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Как сериализовать тело (value) сообщени
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // Гарантирует, что сообщение записано на все реплики
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        // Гарантирует "ровно одно" сообщение
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        // Повторные попытки при временных ошибках
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     *
     * KafkaTemplate Даёт простой способ отправлять сообщения в любом сервисе
     * @return
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
