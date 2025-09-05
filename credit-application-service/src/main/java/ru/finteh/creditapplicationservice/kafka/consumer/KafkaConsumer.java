package ru.finteh.creditapplicationservice.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.finteh.creditapplicationservice.kafka.dto.consumer.VerificationResultEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(topics = "customer-verification-completed")
    public void verificationResult(VerificationResultEvent event){
        log.info("Получено сообщение о проверке данных пользователя {}", event.toString());
    }
}
