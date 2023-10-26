package com.ucb.edu.msmail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.modelmapper.ModelMapper;
@EnableKafka
@Configuration
public class KafkaConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
