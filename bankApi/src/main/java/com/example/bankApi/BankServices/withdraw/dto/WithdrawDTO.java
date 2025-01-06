package com.example.bankApi.BankServices.withdraw.dto;

import java.math.BigDecimal;

public record WithdrawDTO(String document,BigDecimal value) {
}
