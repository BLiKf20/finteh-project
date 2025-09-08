package ru.finteh.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.finteh.userservice.dto.response.GetUserAccountDataResponseDto;
import ru.finteh.userservice.exception.handler.ErrorResponseDto;
import ru.finteh.userservice.service.UserService;

import java.util.UUID;

/**
 * Контроллер для работы с клиентами.
 * <p>
 * Предоставляет конечные точки для получения информации о клиентах.
 */

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Контроллер для отображения данных сохраненных пользователем в личном кабинете
     *
     * @param clientId Id пользователя
     */
    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Отображение данных сохраненных пользователем в личном кабинете")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
        @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                     content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))}),
        @ApiResponse(responseCode = "404", description = "Записи не найдены",
                     content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))}),
        @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера",
                     content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    public ResponseEntity<GetUserAccountDataResponseDto> getSavedUserData(@RequestHeader UUID clientId) {
        GetUserAccountDataResponseDto responseDto = userService.getAccountUserData(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
