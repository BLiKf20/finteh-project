package ru.finteh.creditapplicationservice.service;

import ru.finteh.creditapplicationservice.dto.request.CreateCreditRequestDto;
import ru.finteh.creditapplicationservice.dto.response.CreateCreditResponseDto;
import ru.finteh.creditapplicationservice.dto.response.CreditStatusResponseDto;

import java.util.UUID;

/**
 * Интерфейс для работы с кредитами
 */

public interface CreditService {

    /**
     * Заведение новой заявки на кредит
     * @param createCreditRequestDto - входящий DTO с параметрами для заведения новой заявки на кредит
     * @return возвращаем параметры обрботанной заявки
     */
    CreateCreditResponseDto createNewCredit(CreateCreditRequestDto createCreditRequestDto);

    CreditStatusResponseDto getCreditStatus(UUID id);

    void cancelCreditById(UUID creditId);
}
