package com.example.bankApi.BankConfigurations.MicroServices.producer;

import com.example.bankApi.BankConfigurations.Settings.BalanceSettings;
import com.example.bankApi.BankServices.transactions.models.TransactionModel;
import com.example.bankApi.BankConfigurations.MicroServices.model.EmailDTO;
import com.example.bankApi.User.services.AccountService;
import com.example.bankApi.BankConfigurations.Settings.CpfSettings;
import com.example.bankApi.User.userSettings.UserSettings;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TransactionProducer {

    private final AccountService accountService;
    private final RabbitTemplate rabbitTemplate;
    private final CpfSettings settings;
    private final BalanceSettings balanceSettings;
    private final UserSettings userSettings;

    public TransactionProducer(AccountService accountService,RabbitTemplate rabbitTemplate,CpfSettings settings,
                               BalanceSettings balanceSettings,UserSettings userSettings) {

        this.accountService = accountService;
        this.rabbitTemplate = rabbitTemplate;
        this.settings = settings;
        this.balanceSettings = balanceSettings;
        this.userSettings = userSettings;
    }

    @Value("${broker.queue.email.name}")
    private String routingKey;

    public void publishTransactionServiceEmailSender(TransactionModel transactionModel) {

        var email = new EmailDTO();

        var sender = accountService.findByDocument(transactionModel.getSenderDocument()).get();
        var receiver = accountService.findByDocument(transactionModel.getReceiverDocument()).get();

        var senderBalance = sender
                .getBalance()
                .subtract(transactionModel.getValue());


        String message = userSettings.getFirstName(sender.getUsername()).toUpperCase() +
                ", your transaction has been made successfully! \n\n" +
                "You've SENT: R$"+ transactionModel.getValue() +
                " \n\nTo : " + receiver.getUsername().toUpperCase()+
                " (CPF: "+ settings.format(receiver.getDocument()) +").\n" +
                "Your current balance is now: "+ balanceSettings.format(senderBalance)+"\n" +
                "\nTransaction ID:  "+ transactionModel.getTransactionId() + "\n" +
                "Occured at: "+ LocalDateTime.now()  +
                "\nHaven't sent any money? Contact Us.\n"+
                "\nThanks for using our bank!";

        email.setUserId(sender.getId());
        email.setEmailTo(sender.getEmail());
        email.setSubject("Transaction made successfully!");
        email.setText(message);

        rabbitTemplate.convertAndSend("", routingKey, email);
    }

    public void publishTransactionServiceEmailReceiver(TransactionModel transactionModel) {

        var email = new EmailDTO();

        var sender = accountService.findByDocument(transactionModel.getSenderDocument()).get();
        var receiver = accountService.findByDocument(transactionModel.getReceiverDocument()).get();

        var receiverBalance = receiver.
                getBalance()
                .subtract(transactionModel.getValue());

        String message = userSettings.getFirstName(receiver.getUsername()) +
                ", your transaction has been made successfully! \n" +
                "You RECEIVED: R$"+ transactionModel.getValue() +
                " \nFrom: " + sender.getUsername()+
                "(CPF:"+ settings.format(sender.getDocument()) +").\n" +
                "Your current balance is now: R$"+ balanceSettings.format(receiverBalance) +"\n" +
                "\nTransaction ID:  "+ transactionModel.getTransactionId() + "\n"+
                "Occured at: "+ LocalDateTime.now() + "\n" +
                "Thanks for using our bank!";

        email.setUserId(receiver.getId());
        email.setEmailTo(receiver.getEmail());
        email.setSubject("Money has Arrived!");
        email.setText(message);

        rabbitTemplate.convertAndSend("", routingKey, email);
    }

}
