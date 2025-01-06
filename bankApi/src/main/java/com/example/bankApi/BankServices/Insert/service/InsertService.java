package com.example.bankApi.BankServices.Insert.service;

import com.example.bankApi.BankConfigurations.MicroServices.producer.TypeProducer;
import com.example.bankApi.BankServices.Insert.dto.InsertDTO;
import com.example.bankApi.BankServices.Insert.models.InsertModel;
import com.example.bankApi.BankServices.Insert.repositories.InsertRepository;
import com.example.bankApi.User.models.UserModel;
import com.example.bankApi.User.services.AccountService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InsertService {

    private final InsertRepository repository;
    private final AccountService accountService;
    private final TypeProducer producer;

    public InsertService(InsertRepository repository, AccountService accountService,TypeProducer producer) {
        this.repository = repository;
        this.accountService = accountService;
        this.producer = producer;
    }

    public InsertModel save(InsertDTO data) {
        InsertModel model = new InsertModel();
        UserModel user = accountService.findByDocument(data.document()).get();
        user.setBalance(user.getBalance().add(data.value()));

        model.setUserId(user.getId());
        model.setUserName(user.getUsername());
        model.setUserDocument(user.getDocument());
        model.setValue(data.value());
        model.setUserBalance(user.getBalance());

        producer.publishInsertMail(model);

        return repository.save(model);
    }

    public Optional<InsertModel> findByUserDocument(String userDocument) {
        return repository.findByUserDocument(userDocument);
    }

    public List<InsertModel> findAll() {
        return repository.findAll();
    }

    public List<InsertModel> findAllByUserDocument(String document) {
        List<InsertModel> allInsert = repository.findAll();
        List<InsertModel> myInserts = new ArrayList<InsertModel>();

        for (InsertModel model : allInsert) {
            if(model.getUserDocument().equals(document)) {
                myInserts.add(model);
            }
        }

        return myInserts;
    }



}
