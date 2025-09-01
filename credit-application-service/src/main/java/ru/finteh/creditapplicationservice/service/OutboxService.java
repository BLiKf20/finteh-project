package ru.finteh.creditapplicationservice.service;

import java.util.UUID;

public interface OutboxService {

    /**
     * Метод для сохранения сообщения в БД перед отправкой
     * @param aggregateType - тип бизнес сущности ("CREDIT_APPLICATION", "ORDER", "CUSTOMER").
     *                      Определяет, к какой бизнес-сущности (агрегату) относится событие.
     * @param aggregateId   - идентификатор конкретной сущности.
     *                     Указывает, какой именно экземпляр агрегата породил событие.
     * @param eventType     - тип события ("CREDIT_APPLICATION_CREATED", "CUSTOMER_VERIFIED", "FUNDS_RESERVED").
     *                      Описывает тип доменного события, которое произошло.
     * @param eventPayload  - содержимое события (тело). Назначение: Само событие в формате JSON, сериализованное из Java-объекта.
     * @param topic         - имя Kafka-топика, куда нужно отправить
     */

    void saveOutboxEvent(String aggregateType, UUID aggregateId, String eventType,
                         Object eventPayload, String topic);
}
