package ru.finteh.creditapplicationservice.kafka.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.finteh.creditapplicationservice.model.OutboxEvent;
import ru.finteh.creditapplicationservice.repository.OutboxRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class OutboxEventPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 50000)
    @Transactional
    public void publishOutboxEvents() {
        List<OutboxEvent> outboxEvents = outboxRepository.findByProcessedAtIsNull();

        for (OutboxEvent outboxEvent : outboxEvents) {
            try {
                Object deserialized = objectMapper.readValue(outboxEvent.getPayload(), Object.class);
                kafkaTemplate.send(outboxEvent.getTopic(), String.valueOf(outboxEvent.getAggregateId()), deserialized);
                outboxEvent.setProcessedAt(LocalDateTime.now());

                outboxRepository.save(outboxEvent);
                log.info("Отправляем сообщение в Kafka: topic={}, id={}", outboxEvent.getTopic(), outboxEvent.getId());
            } catch (Exception e) {
                log.error("Не удалось отправить сообщение в Kafka: id={}", outboxEvent.getId(), e);
                //TODO настроить счетчик retry, нужно будет добавить поле в БД для отслеживания кол-ва отправлений
            }
        }
    }

}
