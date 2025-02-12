package com.example.bankApi.BankConfigurations.SecurityConfigurations.security;

import com.example.bankApi.User.models.UserModel;
import com.example.bankApi.User.services.AccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    AccountService accountService;
    TokenService tokenService;

    public SecurityFilter(AccountService accountService, TokenService tokenService) {
        this.accountService = accountService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {

        var token = recoverToken(request);
        var login = tokenService.validateToken(token);

        if (login != null) {
            UserModel userModel = accountService.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Error: User not found or the key is duplicated"));

            var authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_"+userModel.getRole().toString()));
            var authentication = new UsernamePasswordAuthenticationToken(userModel, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null ) return null;
        return authHeader.replace("Bearer ", "");
    }
}
