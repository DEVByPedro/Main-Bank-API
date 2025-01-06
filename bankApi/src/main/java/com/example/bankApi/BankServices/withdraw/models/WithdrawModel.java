package com.example.bankApi.BankServices.withdraw.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "INSERT_TABLE")
public class WithdrawModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID withdrawId;
    private String userDocument;
    private BigDecimal value;
    private LocalDateTime time;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public UUID getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(UUID withdrawId) {
        this.withdrawId = withdrawId;
    }

    public String getUserDocument() {
        return userDocument;
    }

    public void setUserDocument(String userDocument) {
        this.userDocument = userDocument;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
