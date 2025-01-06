package com.example.bankApi.BankServices.Insert.dto;

import java.math.BigDecimal;

public record InsertDTO(String document,
                        BigDecimal value) {
}
