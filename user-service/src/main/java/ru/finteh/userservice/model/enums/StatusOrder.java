package ru.finteh.userservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Перечисление статусов заявки.
 * <p>
 * Данное перечисление определяет возможные статусы заявки.
 * Каждый тип статуса имеет описание, которое предоставляется в виде строки.
 */
@AllArgsConstructor
@Getter
public enum StatusOrder {
    IN_PROCESSING("Заявка в обработке"),
    REJECTED("Отказ по заявке"),
    APPROVED("Заявка одобрена");

    private final String description;
}
