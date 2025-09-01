package ru.finteh.creditapplicationservice.kafka.dto.producer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Dto который будет отправляться в Kafka
 *
 * @param creditOrderId - id заявки на кредит
 * @param clientId      - id клиента
 * @param amount        - запрашиваемая сумма
 * @param termMonths    - запрашиваемый период
 * @param createdAt     - дата создания заявки
 */

@Schema(description = "Создание заявки")
public record CreditApplicationCreatedEvent(

    @Schema(description = "Id заявки на кредит", example = "010b964f-188b-427c-8922-81267997939a")
    @NotNull(message = "Id заявки на кредит не может быть пустым")
    UUID creditOrderId,

    @Schema(description = "Id клиента", example = "e03b1171-20b6-49a9-9c06-bf30ba832fec")
    @NotNull(message = "Id клиента не может быть пустым")
    UUID clientId,

    @Schema(description = "Запрашиваемая сумма", example = "210000")
    @NotNull(message = "Запрашиваемая сумма не может быть пустой")
    BigDecimal amount,

    @Schema(description = "Запрашиваемый период кредита в месяцах", example = "24")
    @NotNull(message = "Запрашиваемый период кредита в месяцах не может быть пустым")
    Integer termMonths,

    @Schema(description = "Дата создания заявки", example = "2025-04-05T10:00:00Z")
    @NotNull(message = "Дата создания заявки не может быть пустой")
    LocalDateTime createdAt
) {

}