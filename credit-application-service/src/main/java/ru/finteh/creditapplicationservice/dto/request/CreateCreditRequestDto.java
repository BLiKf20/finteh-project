package ru.finteh.creditapplicationservice.dto.request;

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

    @NotNull(message = "Поле clientId не может быть null")
    UUID clientId,

    @NotNull(message = "Поле запрашиваемая сумма не может быть null")
    BigDecimal amount,

    @NotNull(message = "Поле запрашиваемый срок кредита не может быть null")
    Integer termMonths
) {

}
