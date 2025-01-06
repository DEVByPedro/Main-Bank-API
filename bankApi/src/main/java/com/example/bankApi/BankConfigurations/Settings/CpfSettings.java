package com.example.bankApi.BankConfigurations.Settings;

import org.springframework.stereotype.Component;

@Component
public class CpfSettings {

    public String format(String cpf) {
        return new StringBuilder(cpf)
                .insert(3, ".")
                .insert(7, ".")
                .insert(11, "-")
                .toString();
    }
}
