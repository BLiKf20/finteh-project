package ru.finteh.creditapplicationservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.finteh.creditapplicationservice.mapper.OutboxMapper;
import ru.finteh.creditapplicationservice.model.OutboxEvent;
import ru.finteh.creditapplicationservice.repository.OutboxRepository;
import ru.finteh.creditapplicationservice.service.OutboxService;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;

    private final OutboxMapper outboxMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveOutboxEvent(String aggregateType, UUID aggregateId, String eventType,
                                Object eventPayload, String topic) {

        // Сериализуем payload в JSON-строку
        try {
            String payload = objectMapper.writeValueAsString(eventPayload);
            log.info("Сохраняем заявку в Outbox");
            // Создаём OutboxEvent через маппер
            OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(
                aggregateType,
                aggregateId,
                eventType,
                topic,
                payload
            );
            // Сохраняем в БД
            outboxRepository.save(outboxEvent);
            log.info("Заявка с id: {} сохранена в Outbox", outboxEvent.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при сериализации события для outbox",e);
        }

    }
}
