package ru.finteh.creditapplicationservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.finteh.creditapplicationservice.model.enums.CreditStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Dto с ответом по созданной заявке на кредит пользователя
 * @param creditOrderId - id заявки на кредит
 * @param clientId      - id пользователя
 * @param amount        - запрошенная сумма
 * @param termMonths    - запрошенный срок для кредита
 * @param status        - статус кредита
 * @param createdAt     - дата создания заявки на кредит
 */

public record CreateCreditResponseDto(

    @Schema(description = "id заявки на кредит", example = "7fa19546-fb3f-45f1-bfcb-755fbd7e8d6a")
    @NotNull(message = "Поле creditOrderId не может быть null")
    UUID creditOrderId,

    @Schema(description = "id клиента", example = "7fa19546-fb3f-45f1-bfcb-755fbd7e8d6a")
    @NotNull(message = "Поле clientId не может быть null")
    UUID clientId,

    @Schema(description = "Запрашиваемая сумма кредита (тыс.руб)", example = "550_000")
    @NotNull(message = "Поле запрашиваемая сумма не может быть null")
    BigDecimal amount,

    @Schema(description = "Запрашиваемый срок кредита в месяцах", example = "24")
    @NotNull(message = "Поле запрашиваемый срок кредита не может быть null")
    Integer termMonths,

    @Schema(description = "Статус кредита", example = "APPROVED")
    @NotNull(message = "Поле статус кредита не может быть null")
    CreditStatus status,

    @Schema(description = "Дата создания заявки на кредит", example = "2025-04-05T10:00:00Z")
    @NotNull(message = "Дата создания заявки не может быть пустой")
    LocalDateTime createdAt
) {

}
