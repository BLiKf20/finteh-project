package ru.finteh.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое при долгом выполении запроса.
 */
@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class RequestTimeOutException extends RuntimeException{

    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public RequestTimeOutException(String message) {
        super(message);
    }
}