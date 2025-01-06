package com.example.bankApi.BankServices.transactions.controllers;

import com.example.bankApi.BankServices.transactions.dto.TransactionDTO;
import com.example.bankApi.BankServices.transactions.models.TransactionModel;
import com.example.bankApi.BankConfigurations.MicroServices.producer.TransactionProducer;
import com.example.bankApi.User.services.AccountService;
import com.example.bankApi.BankServices.transactions.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("services")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final TransactionProducer transactionProducer;

    public TransactionController(TransactionService transactionService, AccountService accountService, TransactionProducer transactionProducer) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.transactionProducer = transactionProducer;
    }

    // POST MAPPING

    @PostMapping("/transaction")
    public ResponseEntity<Object> transactionMethod(@RequestBody @Valid TransactionDTO data) {
            return accountService.findByDocument(data.senderDocument()).<ResponseEntity<Object>>
                            map(account -> ResponseEntity.ok(transactionService.updateData(data)))
                    .orElseGet(() -> ResponseEntity.badRequest().body("Something went wrong"));
    }

    // GET MAPPING

    @GetMapping("/getAllTransaction")
    public List<TransactionModel> getAllTransactions() {
        return transactionService.findAll();
    }

    @GetMapping("/getTransaction={id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id")Long id){
        return transactionService.findById(id).<ResponseEntity<Object>>
                map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    // DELETE MAPPING

    @DeleteMapping("/deleteTransaction={id}")
    public ResponseEntity<Object> deleteTransaction(@PathVariable(value = "id")Long id) {
        return transactionService.findById(id).<ResponseEntity<Object>>
                map(account -> ResponseEntity.ok(transactionService.deleteById(id)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/deleteAllTransactions")
    public void deleteAll() {
        transactionService.deleteAll();
    }


}
