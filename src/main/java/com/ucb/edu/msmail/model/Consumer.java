package com.ucb.edu.msmail.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucb.edu.msmail.dto.EmailOrderDto;
import com.ucb.edu.msmail.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {
    private static final String emailTopic = "${topic.name}";

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @Autowired
    public Consumer(ObjectMapper objectMapper, EmailService emailService) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }

    @KafkaListener(topics = emailTopic)
    public void consumeMessage(String message) throws Exception {
        log.info("message consumed {}", message);

        EmailOrderDto emailOrderDto = objectMapper.readValue(message, EmailOrderDto.class);
        emailService.sendMailWithAttachment("alnzarate@gmail.com", String.format("Body %f", emailOrderDto.getAmount()), String.format("Subject %s", emailOrderDto.getItem()), null);
    }
}
