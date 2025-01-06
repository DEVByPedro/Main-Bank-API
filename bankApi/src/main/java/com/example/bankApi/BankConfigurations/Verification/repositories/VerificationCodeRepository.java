package com.example.bankApi.BankConfigurations.Verification.repositories;

import com.example.bankApi.BankConfigurations.Verification.model.VerificationCodeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCodeModel, UUID> {
    Optional<VerificationCodeModel> findByVerificationCode(String verificationCode);
    Optional<VerificationCodeModel> findByReceiverId(UUID receiverId);
    Optional<VerificationCodeModel> findByReceiverCpf(String receiverCpf);
}
