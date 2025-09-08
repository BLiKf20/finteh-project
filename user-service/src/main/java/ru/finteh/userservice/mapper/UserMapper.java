package ru.finteh.userservice.mapper;

import org.mapstruct.Mapper;
import ru.finteh.userservice.dto.response.GetUserAccountDataResponseDto;
import ru.finteh.userservice.repository.projection.GetUserAccountDataProjection;

/**
 * Маппер для преобразования ответа БД в GetUserAccountDataResponseDto
 *
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    GetUserAccountDataResponseDto toGetUserAccountDataResponseDto(GetUserAccountDataProjection projectionList);
}
