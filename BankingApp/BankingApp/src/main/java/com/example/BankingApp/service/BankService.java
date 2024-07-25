package com.example.BankingApp.service;

import com.example.BankingApp.model.BankAccount;
import com.example.BankingApp.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BankService {

    @Autowired
    private BankAccountRepository repository;

    private Random random = new Random();

    private String generateAccountNumber() {
        String accountNumber;
        do {
            int number = 1000000000 + random.nextInt(900000000); // Generate a random 10-digit number
            accountNumber = String.valueOf(number);
        } while (repository.existsByAccountNumber(accountNumber)); // Ensure the account number is unique
        return accountNumber;
    }

    public String createAccount(String accountHolderName) {
        String accountNumber = generateAccountNumber();
        BankAccount newAccount = new BankAccount(accountNumber, accountHolderName);
        repository.save(newAccount);
        return "Account created successfully. Account Number: " + accountNumber + ", Account Holder: " + accountHolderName;
    }

    public List<BankAccount> viewAllAccounts() {
        return repository.findAll();
    }

    public String deposit(String accountNumber, double amount) {
        Optional<BankAccount> optionalAccount = repository.findByAccountNumber(accountNumber);
        if (optionalAccount.isPresent()) {
            BankAccount account = optionalAccount.get();
            account.deposit(amount); // Use deposit method to update balance
            repository.save(account);
            return "Deposit successful. New Balance: $" + String.format("%.2f", account.getBalance());
        } else {
            return "Account not found.";
        }
    }

    public String withdraw(String accountNumber, double amount) {
        Optional<BankAccount> optionalAccount = repository.findByAccountNumber(accountNumber);
        if (optionalAccount.isPresent()) {
            BankAccount account = optionalAccount.get();
            try {
                account.withdraw(amount); // Use withdraw method to update balance
                repository.save(account);
                return "Withdrawal successful. New Balance: $" + String.format("%.2f", account.getBalance());
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            }
        } else {
            return "Account not found.";
        }
    }

    public String checkBalance(String accountNumber) {
        Optional<BankAccount> optionalAccount = repository.findByAccountNumber(accountNumber);
        if (optionalAccount.isPresent()) {
            BankAccount account = optionalAccount.get();
            return "Account Balance: $" + String.format("%.2f", account.getBalance());
        } else {
            return "Account not found.";
        }
    }
}
