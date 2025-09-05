package ru.finteh.creditapplicationservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationStatus {
    APPROVED("Статус валидации - Одобрено");
    //TODO Добавить статусы валидации клиента

    private final String description;
}
