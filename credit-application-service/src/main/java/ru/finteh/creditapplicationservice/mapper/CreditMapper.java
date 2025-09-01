package ru.finteh.creditapplicationservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.finteh.creditapplicationservice.dto.request.CreateCreditRequestDto;
import ru.finteh.creditapplicationservice.dto.response.CreateCreditResponseDto;
import ru.finteh.creditapplicationservice.dto.response.CreditStatusResponseDto;
import ru.finteh.creditapplicationservice.model.Credit;
import ru.finteh.creditapplicationservice.repository.CreditStatusProjection;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(ru.finteh.creditapplicationservice.model.enums.CreditStatus.CREATED)")
    Credit toCredit(CreateCreditRequestDto createCreditRequestDto);

    @Mapping(target = "creditOrderId", source = "id")
    CreateCreditResponseDto toCreateCreditResponseDto(Credit credit);

    CreditStatusResponseDto toCreditStatusResponseDto(CreditStatusProjection creditStatusProjection);
}
