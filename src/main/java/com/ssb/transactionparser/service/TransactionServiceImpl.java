package com.ssb.transactionparser.service;

import com.ssb.transactionparser.parser.ParserFactory;
import com.ssb.transactionparser.util.JabaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssb.transactionparser.util.JabaConstants.Account;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ssb.transactionparser.util.JabaConstants.Account.*;

@Component
public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    ParserFactory parserFactory;

    @Override
    public void syncTransactions() {
        Account[] accounts = new JabaConstants.Account[] {CHASE_CREDIT, CHASE_CHECKING, DISCOVER,
                CAPITAL_ONE, MACYS, BARCLAYS};

        ExecutorService service = Executors.newFixedThreadPool(4);

        logger.info("Syncing all transactions");

        for(Account account : accounts) {
            service.execute(() -> parserFactory.getTransactionParser(account).syncTransactions());
        }
    }
}
