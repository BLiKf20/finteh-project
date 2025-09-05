package ru.finteh.creditapplicationservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BlacklistCheck {
    PASSED("Проверка пройдена"),
    FAILED("Проверка пройдена неудачно");

    private final String description;
}
