package ru.finteh.creditapplicationservice.dto.response;

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
 * @param createdAt     - дата создания кредита
 */

public record CreateCreditResponseDto(

    UUID creditOrderId,

    UUID clientId,

    BigDecimal amount,

    Integer termMonths,

    CreditStatus status,

    LocalDateTime createdAt
) {

}
