package com.example.bankApi_email.services;

import com.example.bankApi_email.enums.StatusEmail;
import com.example.bankApi_email.models.EmailModel;
import com.example.bankApi_email.repositories.EmailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    final EmailRepository repository;
    final JavaMailSender mailSender;

    public EmailService(EmailRepository repository, JavaMailSender mailSender) {
        this.repository = repository;
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Transactional
    public EmailModel sendEmail(EmailModel emailModel) {
        try {
            emailModel.setSendDateTime(LocalDateTime.now());
            emailModel.setEmailFrom(emailFrom);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            mailSender.send(message);

            emailModel.setStatusEmail(StatusEmail.SENT);
        } catch(MailException e) {
            emailModel.setStatusEmail(StatusEmail.ERROR);
            System.out.println("*********!**********");
            System.out.println(e.getMessage());
            System.out.println("*********!**********");
        } finally {
            return repository.save(emailModel);
        }
    }
}
