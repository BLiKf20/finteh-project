package ru.finteh.creditapplicationservice.kafka.dto.consumer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.finteh.creditapplicationservice.model.enums.ScoringResultReason;

import java.util.UUID;

public record ScoringResult(

    @Schema(description = "Описывает тип доменного события, которое произошло", example = "SCORING_REJECTED")
    @NotNull(message = "evenType не может быть null")
    String eventType,

    @Schema(description = "Это ключ для SAGA, по которому ты найдёшь заявку и продолжишь обработку.", example = "9f7f4be1-c11d-41f9-acdb-d4068c69f83a")
    @NotNull(message = "aggregateId не может быть null")
    UUID aggregateId,

    @Schema(description = "Результат работы скоринг сервиса", example = "HIGH_DEBT_TO_INCOME_RATIO")
    @NotNull(message = "reason не может быть null")
    ScoringResultReason reason,

    @Schema(description = "Рейтинг в баллах от скоринг сервиса", example = "420")
    @NotNull(message = "score не может быть null")
    Integer score
) {

}
