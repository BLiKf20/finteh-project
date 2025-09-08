package ru.finteh.creditapplicationservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO для заведения нового кредита пользователя.
 *
 * @param clientId    - идентификатор клиента
 * @param amount      - запрашиваемая сумма
 * @param termMonths - срок кредита
 */

public record CreateCreditRequestDto(

    @Schema(description = "id клиента", example = "7fa19546-fb3f-45f1-bfcb-755fbd7e8d6a")
    @NotNull(message = "Поле clientId не может быть null")
    UUID clientId,

    @Schema(description = "Запрашиваемая сумма кредита (тыс.руб)", example = "550_000")
    @NotNull(message = "Поле запрашиваемая сумма не может быть null")
    BigDecimal amount,

    @Schema(description = "Запрашиваемый срок кредита в месяцах", example = "24")
    @NotNull(message = "Поле запрашиваемый срок кредита не может быть null")
    Integer termMonths
) {

}
