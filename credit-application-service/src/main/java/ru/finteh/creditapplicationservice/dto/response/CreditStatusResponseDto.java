package ru.finteh.creditapplicationservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Данный Dto возвращает статус кредита по creditId
 * @param status
 */

public record CreditStatusResponseDto(

    @Schema(description = "Отображение статуса кредита", example = "ACTIVE")
    @NotNull(message = "Поле status не может быть null")
    String status
) {

}
