package com.mahesh.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.mahesh.dto.TransactionCreateRequest;
import com.mahesh.models.Transaction;
import com.mahesh.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    public String transact(@RequestBody TransactionCreateRequest request) throws JsonProcessingException {
        return transactionService.transact(request);
    }
}
