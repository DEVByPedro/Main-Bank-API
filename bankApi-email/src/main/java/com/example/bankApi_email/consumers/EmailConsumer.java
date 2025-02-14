package com.example.bankApi_email.consumers;

import com.example.bankApi_email.dto.EmailDTO;
import com.example.bankApi_email.models.EmailModel;
import com.example.bankApi_email.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues="${broker.queue.email.name}")
    public void listenerEmailQueue(@Payload EmailDTO emailRecordDTO) {
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(emailRecordDTO, emailModel);
        emailService.sendEmail(emailModel);
    }
}
