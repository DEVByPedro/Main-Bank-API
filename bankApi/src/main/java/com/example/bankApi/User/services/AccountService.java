package com.example.bankApi.User.services;

import com.example.bankApi.BankConfigurations.Verification.repositories.VerificationCodeRepository;
import com.example.bankApi.BankConfigurations.Verification.services.VerificationService;
import com.example.bankApi.BankServices.transactions.models.TransactionModel;
import com.example.bankApi.User.models.UserModel;
import com.example.bankApi.BankConfigurations.MicroServices.producer.EmailProducer;
import com.example.bankApi.User.repositories.TransactionRepository;
import com.example.bankApi.User.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final EmailProducer emailProducer;
    private final VerificationCodeRepository codeRepository;

    public AccountService(UserRepository userRepository,
                          TransactionRepository transactionRepository,
                          EmailProducer emailProducer,
                          VerificationCodeRepository codeRepository) {

        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.emailProducer = emailProducer;
        this.codeRepository = codeRepository;
    }

    public boolean validateTransaction(BigDecimal balance, BigDecimal value) {
        return balance.subtract(value).intValue() >= 0;
    }

    public UserModel save(UserModel userModel, boolean isTransaction) {
        userRepository.save(userModel);
        if (!isTransaction) {
            emailProducer.publishEmailMessage(userModel);
        }
        return userModel;
    }

    public TransactionModel saveTransaction(TransactionModel transactionModel) { return this.transactionRepository.save(transactionModel); }

    public Optional<UserModel> getOne(String username) {
        return this.userRepository.findByusername(username);
    }

    public Optional<UserModel> findByDocument(String document) {
        return this.userRepository.findByDocument(document);
    }

    public List<UserModel> getAll() {
        return this.userRepository.findAll();
    }

    public Optional<UserModel> findByLogin(String login){
        return this.userRepository.findByusername(login);
    }

    public Optional<UserModel> findById(UUID id) { return userRepository.findById(id); }

    public String deleteByDocument(String document) {
        var user = userRepository.findByDocument(document).get();
        userRepository.delete(user);
        return "";
    }

}
