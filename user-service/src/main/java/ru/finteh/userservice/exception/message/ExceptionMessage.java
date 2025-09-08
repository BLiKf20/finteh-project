package ru.finteh.userservice.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Перечисление ExceptionMessage предоставляет сообщения об исключениях, используемые в приложении для обработки
 * различных ошибок.
 */
@Getter
@AllArgsConstructor
public enum ExceptionMessage {

    INCORRECT_PASSWORD("Введен неверный пароль"),
    ACCOUNT_DOES_NOT_EXISTS("Аккаунт не существует."),
    INTERNAL_SERVER_ERROR("Внутренняя ошибка сервера."),
    REQUEST_TIMEOUT("Запрос выполняется слишком долго."),
    INCORRECT_CODE_ENTERED("Введен неверный код"),
    MANY_REQUEST("Превышено количество попыток ввода"),
    FAILED_SERIALIZE_MESSAGE("Не удалось сериализовать сообщение, причина: %s."),
    NO_RECORD_IN_THE_SYSTEM("Запись отсутствует в системе"),
    BAD_REQUEST("Некорректные данные запроса"),
    FAILED_DESERIALIZE_MESSAGE("Не удалось десериализовать сообщение, причина: %s."),
    DOCUMENTS_NOT_FOUND("Документы по запросу не найдены."),
    CLIENT_BY_MOBILE_PHONE_NOT_FOUND("Мобильный телефон %s не найден."),
    CLIENT_BY_CARD_NUMBER_NOT_FOUND("Номер карты %s не найден."),
    ONLY_ONE_OF_THE_FIELDS_MUST_BE_FILLED_IN("Должно быть заполнено только одно из полей"),
    CLIENT_WITH_THIS_ID_DOES_NOT_EXIST("Клиент с таким id не зарегистрирован."),
    USER_PROFILE_WITH_THIS_ID_DOES_NOT_EXIST("Профиль пользователя с таким id не зарегистрирован."),
    FULL_NAME_NOT_FOUND("Имя и фамилия для пользователя с id = %s не найдены."),
    COULD_NOT_GENERATE_TOKENS("Не удалось сгенерировать jwt токены по причине."),
    CLIENT_WITH_PASSPORT_NUMBER_EXIST("Клиент с таким номером паспорта уже существует."),
    AUTHORIZATION_PARAMS_NOT_VALID("Некорректные запрос на авторизацию, номер паспорта и номер телефона null."),
    MOBILE_PHONE_REGISTERED("Пользователь с таким номером уже существует"),
    INVALID_REQUEST_CLIENT_ID("Неверный id клиента = %s в запросе на получение полного имени пользователя."),
    INVALID_PIN_CODE("Некорректный пин-код"),
    INVALID_CONFIRM_PIN_CODE("Проверьте правильность PIN кода в поле для повтора"),
    UNAUTHORIZED_USER("Пользователь не авторизован"),
    DIFFERENT_PASSWORDS("Пароли не совпадают"),
    DENIED_ACCESS("В доступе отказано"),
    PIN_ALREADY_EXISTS("Для данного пользователя пин код уже задан."),
    USER_ACCOUNT_DATA_NOT_FOUND("Данные пользователя не найдены"),
    EMAIL_HAS_ALREADY_USED_BY_ANOTHER_CLIENT("Данный адрес электронной почты уже используется"),
    EMAIL_SHOULD_BE_VALID("Адрес электронной почты должен быть валидным"),
    EMAIL_COULD_NOT_BE_BLANK("Адрес электронной почты не может быть пустым");

    private final String description;
}