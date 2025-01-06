package com.example.bankApi.BankServices.Insert.controllers;

import com.example.bankApi.BankConfigurations.MicroServices.producer.EmailProducer;
import com.example.bankApi.BankConfigurations.MicroServices.producer.TypeProducer;
import com.example.bankApi.BankServices.Insert.dto.InsertDTO;
import com.example.bankApi.BankServices.Insert.models.InsertModel;
import com.example.bankApi.BankServices.Insert.service.InsertService;
import com.example.bankApi.User.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("insertation-service")
public class InsertController {

    private final InsertService service;
    private final AccountService accountService;
    private final TypeProducer producer;

    public InsertController(InsertService service, AccountService accountService, TypeProducer producer) {
        this.service = service;
        this.accountService = accountService;
        this.producer = producer;
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> insert(@RequestBody @Valid InsertDTO data) {
        return accountService.findByDocument(data.document()).<ResponseEntity<Object>>
                map(userModel -> ResponseEntity.ok(service.save(data)))
                .orElseGet(() -> ResponseEntity.badRequest().body("Something went wrong"));
    }

    @GetMapping("/allMyTransactions={document}")
    public List<InsertModel> allMyTransactions(@PathVariable(value = "document") String document) {
        return service.findAllByUserDocument(document);
    }

    @GetMapping("/allTransactions")
    public List<InsertModel> allTransactions() {
        return service.findAll();
    }
}
