package ru.finteh.creditapplicationservice.kafka.dto.consumer;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.finteh.creditapplicationservice.model.enums.BlacklistCheck;
import ru.finteh.creditapplicationservice.model.enums.ValidationStatus;

import java.util.UUID;

public record VerificationResultEvent(

    @Schema(description = "Описывает тип доменного события, которое произошло", example = "CREDIT_APPLICATION_CREATED")
    String eventType,

    @Schema(description = "Это ключ для SAGA, по которому ты найдёшь заявку и продолжишь обработку.", example = "9f7f4be1-c11d-41f9-acdb-d4068c69f83a")
    UUID aggregateId,

    @Schema(description = "Статус валидации клиента", example = "APPROVED")
    ValidationStatus status,

    @Schema(description = "Проверка клиента в черном списке организации", example = "PASSED")
    BlacklistCheck blacklistCheck,

    @Schema(description = "Флаг проверки валидации паспорта", example = "true")
    Boolean passportValid
) {

}
