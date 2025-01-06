package com.example.bankApi.BankServices.withdraw.controllers;

import com.example.bankApi.BankConfigurations.MicroServices.producer.EmailProducer;
import com.example.bankApi.BankServices.withdraw.dto.WithdrawDTO;
import com.example.bankApi.BankServices.withdraw.models.WithdrawModel;
import com.example.bankApi.BankServices.withdraw.services.WithdrawService;
import com.example.bankApi.User.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("withdraw-service")
public class WithdrawController {

    private final WithdrawService service;
    private final AccountService accountService;

    public WithdrawController(WithdrawService service, AccountService accountService) {
        this.service = service;
        this.accountService = accountService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Object> withdraw(@RequestBody @Valid WithdrawDTO data) {
        return accountService.findByDocument(data.document()).<ResponseEntity<Object>>
                map(model -> ResponseEntity.ok(service.copyAndSaveWithdraw(data)))
                .orElseGet(() -> ResponseEntity.badRequest().body("Something went wrong"));
    }

    @GetMapping("/findAllWithdraws")
    public ResponseEntity<Object> findAllWithdraws() {
        return ResponseEntity.ok(service.findAllWithdraws());
    }

    @GetMapping("/findMyWithdraws={document}")
    public ResponseEntity<Object> findMyWithdraws(@PathVariable(value = "document") String document) {
        return accountService.findByDocument(document).<ResponseEntity<Object>>
                map(user -> ResponseEntity.ok(service.findAllMyWithdraws(document)))
                .orElseGet(() -> ResponseEntity.badRequest().body("Something went wrong"));
    }
}
