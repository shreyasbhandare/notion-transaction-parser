package com.ssb.transactionparser.controller;

import com.ssb.transactionparser.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/sync", method = RequestMethod.GET)
    public String syncTransactions() {
        transactionService.syncTransactions();
        return "Success!";
    }
}