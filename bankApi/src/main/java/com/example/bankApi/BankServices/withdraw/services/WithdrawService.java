package com.example.bankApi.BankServices.withdraw.services;

import com.example.bankApi.BankConfigurations.MicroServices.producer.EmailProducer;
import com.example.bankApi.BankConfigurations.MicroServices.producer.TypeProducer;
import com.example.bankApi.BankServices.withdraw.dto.WithdrawDTO;
import com.example.bankApi.BankServices.withdraw.models.WithdrawModel;
import com.example.bankApi.BankServices.withdraw.repositories.WithdrawRepository;
import com.example.bankApi.User.models.UserModel;
import com.example.bankApi.User.services.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WithdrawService {

    private final WithdrawRepository repository;
    private final AccountService accountService;
    private final TypeProducer producer;

    public WithdrawService(WithdrawRepository repository, AccountService accountService,TypeProducer producer) {
        this.repository = repository;
        this.accountService = accountService;
        this.producer = producer;
    }

    public WithdrawModel save(WithdrawModel model) {
        return repository.save(model);
    }

    public WithdrawModel copyAndSaveWithdraw(WithdrawDTO data) {
        WithdrawModel model = new WithdrawModel();
        BeanUtils.copyProperties(data, model);
        model.setUserDocument(data.document());
        model.setTime(LocalDateTime.now());

        UserModel user = accountService.findByDocument(model.getUserDocument()).get();
        user.setBalance(user.getBalance().subtract(model.getValue()));

        producer.publishWithdrawMail(model);

        return save(model);
    }

    public Optional<WithdrawModel> findByUserDocument(String document) {
        return repository.findByUserDocument(document);
    }

    public Optional<WithdrawModel> findById(UUID id) {
        return repository.findById(id);
    }

    public List<WithdrawModel> findAllWithdraws() {
        return repository.findAll();
    }

    public List<WithdrawModel> findAllMyWithdraws(String document) {
        UUID userId = accountService.findByDocument(document).get().getId();
        List<WithdrawModel> models = repository.findAll();

        List<WithdrawModel> myWithdraws = new ArrayList<WithdrawModel>();

        for(WithdrawModel withdrawModel : models) {
            if (withdrawModel.getUserDocument().equals(document)) {
                myWithdraws.add(withdrawModel);
            }
        }
        return myWithdraws;
    }
}
