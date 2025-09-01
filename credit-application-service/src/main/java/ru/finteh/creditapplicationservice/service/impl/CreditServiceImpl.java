package ru.finteh.creditapplicationservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.finteh.creditapplicationservice.dto.request.CreateCreditRequestDto;
import ru.finteh.creditapplicationservice.dto.response.CreateCreditResponseDto;
import ru.finteh.creditapplicationservice.dto.response.CreditStatusResponseDto;
import ru.finteh.creditapplicationservice.exception.message.ExceptionMessage;
import ru.finteh.creditapplicationservice.exception.NotFoundException;
import ru.finteh.creditapplicationservice.kafka.dto.producer.CreditApplicationCreatedEvent;
import ru.finteh.creditapplicationservice.mapper.CreditApplicationCreatedEventMapper;
import ru.finteh.creditapplicationservice.mapper.CreditMapper;
import ru.finteh.creditapplicationservice.model.Credit;
import ru.finteh.creditapplicationservice.model.enums.CreditStatus;
import ru.finteh.creditapplicationservice.repository.CreditRepository;
import ru.finteh.creditapplicationservice.repository.CreditStatusProjection;
import ru.finteh.creditapplicationservice.service.CreditService;
import ru.finteh.creditapplicationservice.service.OutboxService;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;
    private final CreditApplicationCreatedEventMapper eventMapper;
    private final OutboxService outboxService;

    @Override
    @Transactional
    public CreateCreditResponseDto createNewCredit(CreateCreditRequestDto createCreditRequestDto) {
        Credit credit = creditMapper.toCredit(createCreditRequestDto);

        creditRepository.save(credit);

        log.info("Заявка на кредит успешно создана");

        CreditApplicationCreatedEvent event = eventMapper.toCreditApplicationCreatedEvent(credit);

            outboxService.saveOutboxEvent(
                "CREDIT_APPLICATION_CREATED",
                credit.getId(),
                "CREDIT_APPLICATION",
                event,
                "credit-applications-created"
            );

        return creditMapper.toCreateCreditResponseDto(credit);
    }

    @Override
    @Transactional(readOnly = true)
    public CreditStatusResponseDto getCreditStatus(UUID id) {
        CreditStatusProjection creditStatusProjections = creditRepository.getStatusById(id);
        if (creditStatusProjections == null) {
            log.warn(ExceptionMessage.NOT_FOUND_CREDIT.getDescription(), id);
            throw new NotFoundException(ExceptionMessage.NOT_FOUND_CREDIT.getDescription());
        }
        return creditMapper.toCreditStatusResponseDto(creditStatusProjections);
    }

    @Override
    @Transactional
    public void cancelCreditById(UUID creditId) {
        Credit credit = creditRepository.findById(creditId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND_CREDIT.getDescription()));
        credit.setStatus(CreditStatus.CANCELLED);
    }
}
