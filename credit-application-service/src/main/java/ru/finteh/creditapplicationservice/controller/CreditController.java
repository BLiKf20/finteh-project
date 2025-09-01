package ru.finteh.creditapplicationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.finteh.creditapplicationservice.dto.request.CreateCreditRequestDto;
import ru.finteh.creditapplicationservice.dto.response.CreateCreditResponseDto;
import ru.finteh.creditapplicationservice.dto.response.CreditStatusResponseDto;
import ru.finteh.creditapplicationservice.service.CreditService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/credit")
public class CreditController {

    private final CreditService creditService;

    @PostMapping()
    public ResponseEntity<CreateCreditResponseDto> createCredit(
        @RequestBody @Valid CreateCreditRequestDto requestCreateCreditDto) {
        CreateCreditResponseDto response = creditService.createNewCredit(requestCreateCreditDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{creditId}")
    public ResponseEntity<CreditStatusResponseDto> getCreditStatus(@PathVariable @Valid UUID creditId) {
        CreditStatusResponseDto responseDto = creditService.getCreditStatus(creditId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("{creditId}/cancel")
    public ResponseEntity<Void> cancelCreditById(@PathVariable @Valid UUID creditId) {
        creditService.cancelCreditById(creditId);
        return ResponseEntity.ok().build();
    }
}
