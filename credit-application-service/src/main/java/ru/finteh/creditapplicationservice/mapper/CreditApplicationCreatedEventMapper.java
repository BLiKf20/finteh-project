package ru.finteh.creditapplicationservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.finteh.creditapplicationservice.kafka.dto.producer.CreditApplicationCreatedEvent;
import ru.finteh.creditapplicationservice.model.Credit;

@Mapper(componentModel = "spring")
public interface CreditApplicationCreatedEventMapper {

    @Mapping(target = "creditOrderId", source = "id")
    CreditApplicationCreatedEvent toCreditApplicationCreatedEvent(Credit credit);
}
