package com.example.bankApi.BankServices.Insert.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TABLE_INSERT_BANK")
public class InsertModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID insertId;
    private UUID userId;
    private String userName;
    private String userDocument;
    private BigDecimal value;
    private BigDecimal userBalance;

    public UUID getInsertId() {
        return insertId;
    }

    public void setInsertId(UUID insertId) {
        this.insertId = insertId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public BigDecimal getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(BigDecimal userBalance) {
        this.userBalance = userBalance;
    }
}
