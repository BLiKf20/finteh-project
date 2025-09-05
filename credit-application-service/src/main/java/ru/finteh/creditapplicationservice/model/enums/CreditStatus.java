package ru.finteh.creditapplicationservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CreditStatus {
    ACTIVE("Кредит выдан, находится в статусе \"действующий\""),
    CANCELLED("Заявка отменена клиентом или системой (до активации)"),
    COMPLETED("Заявка успешно обработана"),
    CREATED("Заявка создана клиентом, но ещё не начата обработка"),
    FUNDS_RESERVED("Денежные средства забронированы"),
    NOTIFIED("Уведомление клиента"),
    REJECTED("Заявка отклонена на любом этапе"),
    SCORING("Заявка передана в скорринг"),
    TIMEOUT("Превышено время ожидания ответа от одного из сервисов (например, БКИ не ответил за 5 мин)"),
    VERIFIED("Заявка проверена");

    private final String description;
}
