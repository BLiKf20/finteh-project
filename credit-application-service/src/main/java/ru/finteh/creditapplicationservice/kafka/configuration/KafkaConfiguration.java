package ru.finteh.creditapplicationservice.kafka.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурационный класс для Kafka, который настраивает
 * Kafka Producer — для отправки сообщений в Kafka,
 * KafkaTemplate — удобный Spring-обёртка для отправки сообщений.
 * Он не отправляет сообщения сам, а настраивает инфраструктуру, через которую другие компоненты будут отправлять события.
 * <p>
 * 1. Приложение запускается
 * 2. Spring видит @Configuration → создаёт бины
 * 3. Создаётся producerFactory с настройками из application.yml
 * 4. Создаётся kafkaTemplate, использующий этот factory
 * 5. KafkaTemplate:
 * - Сериализует ключ в String
 * - Сериализует event в JSON
 * - Отправляет в Kafka через Producer
 */

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * ProducerFactory - Фабрика отправителей
     * Указывает адрес Kafka-кластера (bootstrap-servers)
     * Настраивает сериализацию: ключи как String, значения как JSON
     * Гарантирует надежную доставку: acks=all + idempotence=true
     * Настраивает повторные попытки при ошибках
     * @return ProducerFactory
     */
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
     * ConsumerFactory - Фабрика получателей
     * Указывает адрес кластера
     * Настраивает десериализацию (обратную сериализации)
     * Задает group.id для групповой обработки сообщений
     * @return ConsumerFactory
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        // Адрес Kafka-кластера
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Как сериализовать ключ сообщения
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        // Как сериализовать тело (value) сообщения
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        //
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "credit-application-service");

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    /**
     * KafkaTemplate Даёт простой способ отправлять сообщения в любом сервисе. Основной инструмент отправки
     *
     * @return KafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * KafkaListenerContainerFactory - Фабрика слушателей
     * @param consumerFactory
     * @return ConcurrentKafkaListenerContainerFactory
     */
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
        ConsumerFactory<String, Object> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
