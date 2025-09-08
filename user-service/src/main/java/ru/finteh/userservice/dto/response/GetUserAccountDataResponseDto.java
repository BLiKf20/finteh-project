package ru.finteh.userservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Dto для ответа на запрос на отображение данных сохраненных пользователем в личном кабинете
 */

@Builder
@Schema(description = "Данные, отправляемые в ответ на запрос отображения данных сохраненных пользователем в личном кабинете")
public record GetUserAccountDataResponseDto(
    @Schema(description = "Идентификатор пользователя", example = " 123e4567-e89b-12d3-a456-426655440000")
    String clientId,

    @Schema(description = "Имя пользователя", example = "Иван")
    String firstName,

    @Schema(description = "Фамилия пользователя", example = "Иванов")
    String lastName,

    @Schema(description = "Отчество пользователя", example = "Иванович")
    String middleName,

    @Schema(description = "Номер мобильного телефона", example = "+7(985)258-26-79")
    String mobilePhone,

    @Schema(description = "Электронная почта", example = "test@test.com")
    String email,

    @Schema(description = "Номер паспорта", example = "1104008642")
    String passportNumber,

    @Schema(description = "Страна", example = "Российская Федерация")
    String country,

    @Schema(description = "Город регистрации", example = "Москва")
    String city,

    @Schema(description = "Улица", example = "Береговая")
    String street,

    @Schema(description = "Номер дома", example = "8")
    Integer house,

    @Schema(description = "Корпус", example = "1А")
    String hull,

    @Schema(description = "Номер квартиры", example = "28")
    Integer flat,

    @Schema(description = "Почтовый индекс", example = "105043")
    String postCode
) {

}
