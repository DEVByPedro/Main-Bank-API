package com.example.bankApi.BankConfigurations.Settings;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Component
public class BalanceSettings {

    public String format(BigDecimal value) {
        return new DecimalFormat("#,##0.00").format(value);
    }

}
