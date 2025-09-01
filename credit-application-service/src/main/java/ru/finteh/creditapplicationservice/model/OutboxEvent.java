package ru.finteh.creditapplicationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * id                   - уникальный идентификатор события, генерируется автоматически
 *
 * aggregateType        - тип бизнес сущности ("CREDIT_APPLICATION", "ORDER", "CUSTOMER")
 * Назначение: Определяет, к какой бизнес-сущности (агрегату) относится событие.
 * Зачем:
 * Помогает при маршрутизации и фильтрации.
 * Полезно в системах с несколькими типами агрегатов.
 * Может использоваться в обобщённых обработчиках или UI (например, Kafka UI).
 *
 * aggregate_id         - Идентификатор конкретной сущности
 * Назначение: Указывает, какой именно экземпляр агрегата породил событие.
 * Зачем:
 * Позволяет связать событие с конкретной заявкой, заказом и т.д.
 * Используется как ключ партиционирования в Kafka, чтобы события одной сущности шли в одинаковый partition (сохраняется порядок).
 *
 * event_type           - тип события ("CREDIT_APPLICATION_CREATED", "CUSTOMER_VERIFIED", "FUNDS_RESERVED")
 * Назначение: Описывает тип доменного события, которое произошло.
 * Зачем:
 * Позволяет потребителям понять, что именно случилось.
 * Часто используется в switch или if при обработке событий.
 * Может использоваться для маршрутизации (например, разные обработчики для разных типов).
 *
 * payload              - содержимое события (тело). Назначение: Само событие в формате JSON, сериализованное из Java-объекта.
 *
 * topic                - имя Kafka-топика, куда нужно отправить
 *
 * createdAt            - время создания события
 *
 * processedAt          - Время отправки в Kafka(при создании заявки устанавливаем в null,
 *                        так kafka поймет, что это сообщение еще не отправлено)
 */

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType; // "CREDIT_APPLICATION"

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "event_type", nullable = false)
    private String eventType; // "CREDIT_APPLICATION_CREATED"

    @Column(name = "payload", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)  // Гарантирует правильный тип в Hibernate
    private String payload; // String, но содержит JSON как строку

    @Column(nullable = false)
    private String topic;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;
}

/**
 * 1. Клиент создаёт заявку → сохраняется в credit_applications
 * 2. В той же транзакции создаётся OutboxEvent:
 *    - aggregateType = "CREDIT_APPLICATION"
 *    - aggregateId = [id заявки]
 *    - eventType = "CREDIT_APPLICATION_CREATED"
 *    - payload = { ... }
 *    - topic = "credit-applications-created"
 *    - processedAt = null
 *    - createdAt = now()
 * 3. Транзакция коммитится → оба объекта сохранены
 * 4. Фоновый процесс (OutboxPublisher) находит события с processedAt = null
 * 5. Отправляет каждое в Kafka
 * 6. Устанавливает processedAt = now()
 */
