package com.example.bankApi.BankConfigurations.MicroServices.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /** Jackson2JsonMessageConverter :
     * Uses a objectMapper to convert from broker ( JSON FILE ) to message,
     * and then sends it to microservice, so it can be formated and sent
     * to user by the configuration that is set in it's micro-services.
     */

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
