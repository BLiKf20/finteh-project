package ru.finteh.creditapplicationservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.finteh.creditapplicationservice.model.OutboxEvent;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OutboxMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "processedAt", expression = "java(null)")
    OutboxEvent toOutboxEvent(
        String aggregateType,
        UUID aggregateId,
        String eventType,
        String topic,
        String payload
    );
}
