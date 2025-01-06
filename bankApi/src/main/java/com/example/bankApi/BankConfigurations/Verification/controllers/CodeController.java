package com.example.bankApi.BankConfigurations.Verification.controllers;

import com.example.bankApi.BankConfigurations.Verification.dto.VerificationCodeDTO;
import com.example.bankApi.BankConfigurations.Verification.services.VerificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("two-steps")
public class CodeController {

    private final VerificationService service;

    public CodeController(VerificationService service) {
        this.service = service;
    }

    @PostMapping("/verify")
    public ResponseEntity<Object> verifyCode(@RequestBody @Valid VerificationCodeDTO data) throws RuntimeException {
        return service.findByVerificationCode(data.code()).<ResponseEntity<Object>>
                map(code -> ResponseEntity.ok(service.validateCode(code) + service.deleteCodeById(code.getVerificationCodeId())))
                .orElseGet(() -> ResponseEntity.badRequest().body("Code not found."));
    }
}
