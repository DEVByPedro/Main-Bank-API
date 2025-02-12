package com.example.bankApi.BankConfigurations.SecurityConfigurations.security;

import com.example.bankApi.User.models.UserModel;
import com.example.bankApi.User.services.AccountService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthorizationService implements UserDetailsService {

    private final AccountService accountService;

    public AuthorizationService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userFound = accountService.findByLogin(username).get();

        return new User(
                userFound.getUsername(),
                userFound.getPassword(),
                new ArrayList<>()
        );
    }
}
