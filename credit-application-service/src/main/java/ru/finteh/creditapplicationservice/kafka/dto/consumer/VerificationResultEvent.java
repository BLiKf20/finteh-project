package ru.finteh.creditapplicationservice.kafka.dto.consumer;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.finteh.creditapplicationservice.model.enums.BlacklistCheck;
import ru.finteh.creditapplicationservice.model.enums.ValidationStatus;

import java.util.UUID;

public record VerificationResultEvent(

    @Schema(description = "Описывает тип доменного события, которое произошло", example = "CREDIT_APPLICATION_CREATED")
    String eventType,

    @Schema(description = "Указывает, какой именно экземпляр агрегата породил событие", example = "app-123")
    UUID aggregateId,

    @Schema(description = "Статус валидации клиента", example = "APPROVED")
    ValidationStatus status,

    @Schema(description = "Проверка клиента в черном списке организации", example = "PASSED")
    BlacklistCheck blacklistCheck,

    @Schema(description = "Флаг проверки валидации паспорта", example = "true")
    Boolean passportValid
) {

    @Override
    public String toString() {
        return "Event_type=" + eventType + "; " +
            "aggregateId=" + aggregateId + "; " +
            "ValidationStatus=" + status + "; " +
            "BlacklistCheck=" + blacklistCheck + "; " +
            "PassportValid=" + passportValid;
    }

}
