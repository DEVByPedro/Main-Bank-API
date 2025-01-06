package com.example.bankApi.BankConfigurations.Verification.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "TB_VERIFICATION_CODE")
public class VerificationCodeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID verificationCodeId;

    @Column(unique = true)
    private String verificationCode;

    @Column(unique = true)
    private UUID receiverId;

    @Column(unique = true)
    private String receiverCpf;

    public String getReceiverCpf() {
        return receiverCpf;
    }

    public void setReceiverCpf(String receiverCpf) {
        this.receiverCpf = receiverCpf;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }

    public UUID getVerificationCodeId() {
        return verificationCodeId;
    }

    public void setVerificationCodeId(UUID verificationCodeId) {
        this.verificationCodeId = verificationCodeId;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
