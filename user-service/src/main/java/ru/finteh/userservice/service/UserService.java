package ru.finteh.userservice.service;

import ru.finteh.userservice.dto.response.GetUserAccountDataResponseDto;

import java.util.UUID;

public interface UserService {

    GetUserAccountDataResponseDto getAccountUserData(UUID clientId);
}
