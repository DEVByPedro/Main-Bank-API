package com.example.bankApi.BankServices.withdraw.repositories;

import com.example.bankApi.BankServices.withdraw.models.WithdrawModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WithdrawRepository extends JpaRepository<WithdrawModel, UUID> {
    Optional<WithdrawModel> findByUserDocument(String userDocument);
}
