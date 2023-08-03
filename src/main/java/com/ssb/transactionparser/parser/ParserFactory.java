package com.ssb.transactionparser.parser;

import com.ssb.transactionparser.util.JabaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParserFactory {
    @Autowired
    ChaseCheckingParser chaseCheckingParser;
    @Autowired
    ChaseCreditParser chaseCreditParser;
    @Autowired
    DiscoverParser discoverParser;
    @Autowired
    MacysParser macysParser;
    @Autowired
    CapitalOneParser capitalOneParser;
    @Autowired
    BarclaysParser barclaysParser;

    public TransactionParser getTransactionParser(JabaConstants.Account account) {
        return switch (account) {
            case CHASE_CREDIT -> chaseCreditParser;
            case CHASE_CHECKING -> chaseCheckingParser;
            case DISCOVER -> discoverParser;
            case MACYS -> macysParser;
            case CAPITAL_ONE -> capitalOneParser;
            case BARCLAYS -> barclaysParser;
        };
    }
}
