package com.example.bankApi.User.repositories;

import com.example.bankApi.User.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findByusername(String username);
    Optional<UserModel> findByDocument(String document);
}
