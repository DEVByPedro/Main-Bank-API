package com.example.bankApi.BankServices.Insert.repositories;

import com.example.bankApi.BankServices.Insert.models.InsertModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InsertRepository extends JpaRepository<InsertModel, UUID> {
    Optional<InsertModel> findByUserDocument(String userDocument);
}
