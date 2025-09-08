package ru.finteh.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.finteh.userservice.model.Client;
import ru.finteh.userservice.repository.projection.GetUserAccountDataProjection;

import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс для осуществления операций чтения и записи данных о клиенте банка/пользователя приложения в базу данных
 */

@Repository
public interface UserRepository extends JpaRepository<Client, UUID> {

    /**
     * Метод используется для получения данных сохраненных пользователем в личном кабинете
     *
     * @param clientId идентификатор пользователя
     * @return Optional<GetUserAccountDataProjection> получаемые данные
     */
    @Query
        ("""
            SELECT new ru.finteh.userservice.repository.projection.GetUserAccountDataProjection(
                        c.id,
                        c.firstName,
                        c.lastName,
                        c.middleName,
                        c.mobilePhone,
                        up.email,
                        pd.passportNumber,
                        a.country,
                        a.city,
                        a.street,
                        a.house,
                        a.hull,
                        a.flat,
                        a.postCode
            )
            FROM UserProfile up
            JOIN up.client c
            LEFT JOIN c.passportData pd
            LEFT JOIN c.addressRegistration a
            WHERE c.id = :clientId
            """)
    Optional<GetUserAccountDataProjection> findUserAccountData(UUID clientId);

}
