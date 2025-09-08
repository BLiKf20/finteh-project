package ru.finteh.creditapplicationservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScoringResultReason {

    HIGH_DEBT_TO_INCOME_RATIO("Высокое соотношение долга к доходу");

    private final String description;
}
