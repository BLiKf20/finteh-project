package ru.finteh.creditapplicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.finteh.creditapplicationservice.model.Credit;

import java.util.UUID;

@Repository
public interface CreditRepository extends JpaRepository<Credit, UUID> {

    CreditStatusProjection getStatusById(UUID id);
}
