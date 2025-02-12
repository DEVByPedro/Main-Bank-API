package com.example.bankApi.User.controllers;

import com.example.bankApi.BankConfigurations.Verification.services.VerificationService;
import com.example.bankApi.User.DTO.RegisterDTO;
import com.example.bankApi.User.DTO.UserDTO;
import com.example.bankApi.User.models.UserModel;
import com.example.bankApi.BankConfigurations.MicroServices.producer.EmailProducer;
import com.example.bankApi.User.services.AccountService;
import com.example.bankApi.BankServices.transactions.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final EmailProducer emailProducer;
    private final VerificationService verificationService;

    public UserController(PasswordEncoder passwordEncoder,
                          AccountService accountService,
                          TransactionService transactionService,
                          EmailProducer emailProducer,
                          VerificationService verificationService) {

        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.emailProducer = emailProducer;
        this.verificationService = verificationService;
    }

    /** METHODS
     *  Used as a help when creating / manipulating DATA.
     *  Also acts as a Shortcut.
     */

    public UserModel createData(RegisterDTO data) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(data, userModel);
        userModel.setPassword(passwordEncoder.encode(data.password()));

        return accountService.save(userModel, false);
    }

    public UserModel updateUserData(RegisterDTO data) {
        var userFound = accountService.findByDocument(data.document()).get();

        BeanUtils.copyProperties(data, userFound);
        userFound.setPassword(passwordEncoder.encode(data.password()));

        return accountService.save(userFound, false);
    }

    /**
     * POST MAPPING :
     * Checks if the data defined in Postman exists in database.
     * Used as a JSON file.
     */

    @PostMapping("/login")
    public ResponseEntity<Object> postUser(@RequestBody @Valid UserDTO data) {
        return accountService.findByDocument(data.document()).<ResponseEntity<Object>>map(account -> (passwordEncoder.matches(data.password(), account.getPassword()))
                        ? ResponseEntity.ok(verificationService.isCodeAvailable(account) + emailProducer.publishTwoStepCode(account))
                        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password."))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * POST MAPPING :
     * inserts data defined in Postman into database.
     * Used as a JSON file.
     */

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDTO data) {
        return accountService.findByDocument(data.document()).<ResponseEntity<Object>>
                        map(account -> ResponseEntity.badRequest()
                        .body("The document " + data.document() + "\nhas already been registered!"))
                        .orElseGet(() -> ResponseEntity.ok(createData(data)));
    }

    /** PUT MAPPING :
     *  Updates a UserModel, finding it by it's document, since document is unique.
     */

    @PutMapping("/updateData={document}")
    public ResponseEntity<Object> updateData(@PathVariable(value = "document") String document, @RequestBody @Valid RegisterDTO data) {
        return accountService.findByDocument(document).<ResponseEntity<Object>>
                map(account -> ResponseEntity.ok(updateUserData(data)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /** GET MAPPING :
     * Returns a List of all UserModels from database.
     */

    @GetMapping("/getAllUsers")
    public List<UserModel> getAll() {
        return accountService.getAll();
    }

    /**
     * GET MAPPING : Returns a UserModel
     * finds it by Document, since document is unique.
     */

    @GetMapping("/getUser={document}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "document") String document) {
        return accountService.findByDocument(document).<ResponseEntity<Object>>
                map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("No user with document: " + document + " has been founded."));
    }

    /** DELETE MAPPING :
     * Deletes data from database by Postman, using document as index since it's unique.
     */

    @DeleteMapping("/deleteUser/{document}")
    public ResponseEntity<Object> deleteOne(@PathVariable(value = "document") String document) {
        return accountService.findByDocument(document).<ResponseEntity<Object>>
                map(account -> ResponseEntity.status(HttpStatus.OK)
                        .body("User: " + accountService.findByDocument(document) + "\nhas been deleted with success." +
                                accountService.deleteByDocument(document)))
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body("No user with document: " + document + "\nhas been founded."));
    }
}
