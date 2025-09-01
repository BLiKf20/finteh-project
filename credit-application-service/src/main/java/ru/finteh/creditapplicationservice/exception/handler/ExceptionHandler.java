package ru.finteh.creditapplicationservice.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.finteh.creditapplicationservice.exception.BadRequestException;
import ru.finteh.creditapplicationservice.exception.InternalServerException;
import ru.finteh.creditapplicationservice.exception.NotFoundException;
import ru.finteh.creditapplicationservice.exception.UnauthorizedException;

@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    public ErrorResponseDto handleBadRequestException(RuntimeException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponseDto invalidRequestException(RuntimeException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(InternalServerException.class)
    public ErrorResponseDto handleInternalServerException(RuntimeException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ErrorResponseDto handleNotFoundException(RuntimeException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    public ErrorResponseDto handleUnauthorized(RuntimeException e) {
        return new ErrorResponseDto(e.getMessage());
    }
}