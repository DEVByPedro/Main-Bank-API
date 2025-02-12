package com.example.bankApi.BankServices.transactions.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_TRANSACTIONS")
public class TransactionModel implements Serializable {

    @Id
    private UUID transactionId;
    private String senderDocument;
    private String senderName;
    private String receiverDocument;
    private String receiverName;
    private BigDecimal value;
    private LocalDateTime timeStamp;

    public TransactionModel() {
    }

    public TransactionModel(UUID transactionId,
                            String senderDocument,
                            String senderName,
                            String receiverDocument,
                            String receiverName,
                            BigDecimal value,
                            LocalDateTime timeStamp) {

        this.transactionId = transactionId;
        this.senderDocument = senderDocument;
        this.senderName = senderName;
        this.receiverDocument = receiverDocument;
        this.receiverName = receiverName;
        this.value = value;
        this.timeStamp = timeStamp;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getSenderDocument() {
        return senderDocument;
    }

    public void setSenderDocument(String senderDocument) {
        this.senderDocument = senderDocument;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverDocument() {
        return receiverDocument;
    }

    public void setReceiverDocument(String receiverDocument) {
        this.receiverDocument = receiverDocument;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
