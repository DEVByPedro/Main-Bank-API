package com.example.bankApi.BankConfigurations.Verification.services;

import com.example.bankApi.BankConfigurations.SecurityConfigurations.security.TokenService;
import com.example.bankApi.BankConfigurations.Verification.model.VerificationCodeModel;
import com.example.bankApi.BankConfigurations.Verification.repositories.VerificationCodeRepository;
import com.example.bankApi.User.Enums.UserRole;
import com.example.bankApi.User.models.UserModel;
import com.example.bankApi.User.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class VerificationService {

    private final VerificationCodeRepository repository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public VerificationService(VerificationCodeRepository repository,
                               UserRepository userRepository,
                               TokenService tokenService) {

        this.repository = repository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public Optional<VerificationCodeModel> findByReceiverCpf(String receiverCpf) {
        return repository.findByReceiverCpf(receiverCpf);
    }

    public String isCodeAvailable(UserModel model) {

        if (repository.findByReceiverId(model.getId()).isPresent()) {
            deleteCodeById(repository.findByReceiverId(model.getId()).get().getVerificationCodeId());
        }

        return generateCode(model);
    }

    public String generateCode(UserModel model) {

        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        String chars = "abcddefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        for (int i = 0; i < 8; i++) {
            var randomize = random.nextInt(chars.length());
            var randomizedChar = chars.charAt(randomize);
            stringBuilder.append(randomizedChar);
        }

        VerificationCodeModel verificationCode = new VerificationCodeModel();
        verificationCode.setVerificationCode(stringBuilder.toString());
        verificationCode.setReceiverId(model.getId());
        verificationCode.setReceiverCpf(model.getDocument());

        repository.save(verificationCode);

        return verificationCode.getVerificationCode();
    }

    public String deleteCodeById(UUID id) {
        repository.deleteById(id);
        return "";
    }

    public String validateCode(VerificationCodeModel model) throws RuntimeException{
        var repositoryCode = repository.findByVerificationCode(model.getVerificationCode())
                .orElseThrow(() -> new RuntimeException("Code not Found"));

        UserModel user = userRepository.findByDocument(model.getReceiverCpf()).get();

        if(repositoryCode.getReceiverCpf().equals(user.getDocument())) {
            if (model.getVerificationCode().equals(repositoryCode.getVerificationCode())) {
                if (user.getRole().equals(UserRole.ADMIN)) {
                    return tokenService.generateToken(user);
                }
                return "Welcome";
            }
        }

        return "";
    }

    public Optional<VerificationCodeModel> findByVerificationCode(String code) {
        return repository.findByVerificationCode(code);
    }

}
