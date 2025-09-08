package ru.finteh.userservice.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Проекция для сущности User и связанных с нею (UserProfile, PassportData, Address).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserAccountDataProjection {
    private UUID clientId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String mobilePhone;
    private String email;
    private String passportNumber;
    private String country;
    private String city;
    private String street;
    private String house;
    private String hull;
    private String flat;
    private String postCode;
}

