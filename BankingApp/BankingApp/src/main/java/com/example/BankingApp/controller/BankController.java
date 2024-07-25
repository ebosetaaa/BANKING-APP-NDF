package com.example.BankingApp.controller;

import com.example.BankingApp.model.User;
import com.example.BankingApp.service.BankService;
import com.example.BankingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.sql.*;

@Controller
public class BankController {

    @Autowired
    private BankService bankService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index"; // This will render index.html (or index.html equivalent) in src/main/resources/templates/
    }




    @GetMapping("/create-account")
    public String showCreateAccountForm() {
        return "create-account"; // Render create-account.html form
    }

    @PostMapping("/create-account")
    public String createAccount(@RequestParam("accountHolderName") String accountHolderName, Model model) {
        String message = bankService.createAccount(accountHolderName);
        model.addAttribute("message", message);
        return "account-created"; // Render account-created.html with success message
    }

    @GetMapping("/view-accounts")
    public String viewAllAccounts(Model model) {
        model.addAttribute("accounts", bankService.viewAllAccounts());
        return "view-accounts"; // Render view-accounts.html with list of accounts
    }

    @GetMapping("/deposit")
    public String depositForm() {
        return "deposit"; // Render deposit.html form
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam double amount, Model model) {
        String message = bankService.deposit(accountNumber, amount);
        model.addAttribute("message", message);
        return "result";
    }

    @GetMapping("/withdraw")
    public String withdrawForm(){
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam double amount, Model model) {
        String message = bankService.withdraw(accountNumber, amount);
        model.addAttribute("message", message);
        return "result";
    }

    @GetMapping("/checkBalance")
    public String checkBalanceForm(){
        return "checkBalance";
    }

    @PostMapping("/checkBalance")
    public String checkBalance(@RequestParam String accountNumber, Model model) {
        String message = bankService.checkBalance(accountNumber);
        model.addAttribute("message", message);
        return "result";
    }
}
