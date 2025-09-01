package ru.finteh.creditapplicationservice.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {

    BAD_REQUEST("Некорректный запрос. Ошибка в данных запроса"),
    INTERNAL_SERVER_ERROR("Внутренняя ошибка сервера"),
    UNAUTHORIZED("Доступ запрещен. Необходима авторизация"),
    NOT_FOUND_CREDIT("Кредитные заявки по указанному id не найдены");

    private final String description;
}
