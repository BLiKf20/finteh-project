package ru.finteh.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.finteh.userservice.dto.response.GetUserAccountDataResponseDto;
import ru.finteh.userservice.exception.NotFoundException;
import ru.finteh.userservice.mapper.UserMapper;
import ru.finteh.userservice.repository.UserRepository;
import ru.finteh.userservice.service.UserService;

import java.util.UUID;

import static ru.finteh.userservice.exception.message.ExceptionMessage.USER_ACCOUNT_DATA_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public GetUserAccountDataResponseDto getAccountUserData(UUID clientId) {
        return userRepository.findUserAccountData(clientId)
            .map(userMapper::toGetUserAccountDataResponseDto)
            .orElseThrow(() -> {
                log.warn("Данные пользователя c id={} не найдены", clientId);
                return new NotFoundException(USER_ACCOUNT_DATA_NOT_FOUND.getDescription().formatted(clientId));
            });
    }
}
