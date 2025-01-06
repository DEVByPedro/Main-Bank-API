package com.example.bankApi.BankConfigurations.MicroServices.producer;

import com.example.bankApi.BankConfigurations.Verification.repositories.VerificationCodeRepository;
import com.example.bankApi.BankConfigurations.Verification.services.VerificationService;
import com.example.bankApi.User.models.UserModel;
import com.example.bankApi.BankConfigurations.MicroServices.model.EmailDTO;
import com.example.bankApi.BankConfigurations.SecurityConfigurations.security.TokenService;
import com.example.bankApi.User.userSettings.UserSettings;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;
    private final TokenService tokenService;
    private final VerificationService verificationService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final UserSettings userSettings;

    public EmailProducer(RabbitTemplate rabbitTemplate, TokenService tokenService, VerificationService verificationService,
                         VerificationCodeRepository verificationCodeRepository, UserSettings userSettings) {

        this.rabbitTemplate = rabbitTemplate;
        this.tokenService = tokenService;
        this.verificationService = verificationService;
        this.verificationCodeRepository = verificationCodeRepository;
        this.userSettings = userSettings;
    }

    @Value("${broker.queue.email.name}")
    private String routingKey;

    public void publishEmailMessage(UserModel userModel) {
        var email = new EmailDTO();
        email.setUserId(userModel.getId());
        email.setEmailTo(userModel.getEmail());
        email.setSubject("Success on creating your account!");
        email.setText(userSettings.getFirstName(userModel.getUsername())+
                ", Thanks for registering \n" +
                "Enjoy your brand new bank app!\n" +
                "Your Role: "+userModel.getRole().toString());

        rabbitTemplate.convertAndSend("", routingKey, email);
    }

    public String publishTwoStepCode(UserModel userModel) {

        UUID userId = userModel.getId();
        var code = verificationCodeRepository.findByReceiverId(userId);

        var email = new EmailDTO();
        email.setUserId(userModel.getId());
        email.setEmailTo(userModel.getEmail());
        email.setSubject("2-Steps Verification Code");
        email.setText(userSettings.getFirstName(userModel.getUsername())+
                ", Here is your Two-Steps Verification Code: \n\n"+
                code.get().getVerificationCode());

        rabbitTemplate.convertAndSend("", routingKey, email);

        return "";
    }


}
