package com.example.bankApi.BankConfigurations.MicroServices.producer;

import com.example.bankApi.BankConfigurations.MicroServices.model.EmailDTO;
import com.example.bankApi.BankConfigurations.Settings.BalanceSettings;
import com.example.bankApi.BankServices.Insert.models.InsertModel;
import com.example.bankApi.BankServices.Insert.repositories.InsertRepository;
import com.example.bankApi.BankServices.withdraw.models.WithdrawModel;
import com.example.bankApi.BankServices.withdraw.repositories.WithdrawRepository;
import com.example.bankApi.User.models.UserModel;
import com.example.bankApi.User.services.AccountService;
import com.example.bankApi.User.userSettings.UserSettings;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TypeProducer {

    private final RabbitTemplate rabbitTemplate;
    private final WithdrawRepository withdrawRepository;
    private final InsertRepository insertRepository;
    private final AccountService accountService;
    private final UserSettings userSettings;

    public TypeProducer(RabbitTemplate rabbitTemplate, WithdrawRepository withdrawRepository,
                        InsertRepository insertRepository, AccountService accountService, UserSettings userSettings) {
        this.rabbitTemplate = rabbitTemplate;
        this.withdrawRepository = withdrawRepository;
        this.insertRepository = insertRepository;
        this.accountService = accountService;
        this.userSettings = userSettings;
    }

    @Value("${broker.queue.email.name}")
    private String routingKey;

    public String publishWithdrawMail(WithdrawModel model) {

        BalanceSettings settings = new BalanceSettings();
        UserModel user = accountService.findByDocument(model.getUserDocument()).get();

        var email = new EmailDTO();
        email.setUserId(user.getId());
        email.setEmailTo(user.getEmail());
        email.setSubject("Withdraw made!");
        email.setText(userSettings.getFirstName(user.getUsername())+", you made a withdraw of value: R$"+settings.format(model.getValue())+"\n" +
                "Your current bank balance: R$"+settings.format(user.getBalance())+"\n"+
                "Occured at: "+LocalDateTime.now()+"\n"+
                "Your ID: "+user.getId());

        rabbitTemplate.convertAndSend("", routingKey, email);

        return "";
    }

    public String publishInsertMail(InsertModel model) {
        BalanceSettings settings = new BalanceSettings();
        UserModel user = accountService.findByDocument(model.getUserDocument()).get();

        var email = new EmailDTO();
        email.setUserId(model.getUserId());
        email.setEmailTo(user.getEmail());
        email.setSubject("Deposit made!");
        email.setText(userSettings.getFirstName(user.getUsername())+", you made a deposit of value: R$"+settings.format(model.getValue())+"\n" +
                "Your current bank balance: R$"+settings.format(user.getBalance())+ "\n"+
                "Occured at: "+ LocalDateTime.now()+"\n" +
                "Your ID: "+ user.getId());

        rabbitTemplate.convertAndSend("", routingKey, email);

        return "";
    }
}
