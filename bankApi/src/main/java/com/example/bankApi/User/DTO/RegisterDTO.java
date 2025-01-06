package com.example.bankApi.User.DTO;

import com.example.bankApi.User.Enums.UserRole;

import java.math.BigDecimal;

public record RegisterDTO(String username,
                          String password,
                          String email,
                          String document,
                          BigDecimal balance,
                          UserRole role) {
}
