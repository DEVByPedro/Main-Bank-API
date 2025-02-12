package com.example.bankApi.BankServices.transactions.services;

import com.example.bankApi.BankServices.transactions.dto.TransactionDTO;
import com.example.bankApi.BankServices.transactions.models.TransactionModel;
import com.example.bankApi.BankConfigurations.MicroServices.producer.TransactionProducer;
import com.example.bankApi.User.repositories.TransactionRepository;
import com.example.bankApi.User.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final TransactionProducer transactionProducer;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountService accountService,
                              TransactionProducer transactionProducer) {

        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.transactionProducer = transactionProducer;
    }

    public boolean authorizationService(BigDecimal balance, BigDecimal value) {
        return balance.subtract(value).intValue() >= 0;
    }

    public TransactionModel save(TransactionModel transactionModel) {
        return transactionRepository.save(transactionModel);
    }

    public TransactionModel updateData(TransactionDTO data){
        var newSender = this.accountService.findByDocument(data.senderDocument()).get();
        var newReceiver = this.accountService.findByDocument(data.receiverDocument()).get();

        boolean isAuthorized = authorizationService(newSender.getBalance(), data.value());
        if (!isAuthorized) {
            throw new RuntimeException("Transaction not allowed.");
        }

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTransactionId(UUID.randomUUID());
        transactionModel.setSenderDocument(data.senderDocument());
        transactionModel.setSenderName(accountService.findByDocument(data.senderDocument()).get().getUsername());
        transactionModel.setReceiverDocument(data.receiverDocument());
        transactionModel.setReceiverName(accountService.findByDocument(data.receiverDocument()).get().getUsername());
        transactionModel.setValue(data.value());
        transactionModel.setTimeStamp(LocalDateTime.now());

        newSender.setBalance(newSender.getBalance().subtract(data.value()));
        newReceiver.setBalance(newReceiver.getBalance().add(data.value()));

        accountService.save(newSender, true);
        accountService.save(newReceiver, true);

        transactionProducer.publishTransactionServiceEmailSender(transactionModel);
        transactionProducer.publishTransactionServiceEmailReceiver(transactionModel);

        return transactionRepository.save(transactionModel);
    }

    public List<TransactionModel> findAll() {
        return transactionRepository.findAll();
    }

    public ResponseEntity<Object> deleteAll() {
        transactionRepository.deleteAll();
        return ResponseEntity.ok("Deleted All Transactions");
    }

    public Optional<TransactionModel> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public ResponseEntity<Object> deleteById(Long id) {
        transactionRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
