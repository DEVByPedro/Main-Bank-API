package com.example.bankApi.BankServices.transactions.dto;

import java.math.BigDecimal;

public record TransactionDTO(String senderDocument,
                             String receiverDocument,
                             BigDecimal value) {
}
