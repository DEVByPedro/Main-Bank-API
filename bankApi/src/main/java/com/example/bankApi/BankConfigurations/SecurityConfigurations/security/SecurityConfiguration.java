package com.example.bankApi.BankConfigurations.SecurityConfigurations.security;

import com.example.bankApi.User.Enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthorizationService authorizationService;
    private final SecurityFilter securityFilter;

    public SecurityConfiguration(AuthorizationService authorizationService, SecurityFilter securityFilter) {
        this.authorizationService = authorizationService;
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "user/getAllUsers").hasRole(UserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.PUT, "user/updateData={document}").hasRole(UserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "user/deleteUser={document}").hasRole(UserRole.ADMIN.toString())

                        .requestMatchers(HttpMethod.DELETE, "services/deleteAllTransactions").hasRole(UserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "services/deleteTransaction={id}").hasRole(UserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.GET, "services/getTransaction={id}").hasRole(UserRole.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "withdraw-service/findAllWithdraws").hasRole(UserRole.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "insertation-service/allTransactions").hasRole(UserRole.ADMIN.toString())
                        .anyRequest().permitAll()
                )

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
