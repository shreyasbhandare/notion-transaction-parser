package com.ssb.transactionparser.parser;

import com.ssb.transactionparser.model.Transaction;
import com.ssb.transactionparser.notion.NotionClientWrapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.ssb.transactionparser.util.CategoryFinderUtil.findCategoryFromDescription;
import static com.ssb.transactionparser.util.JabaConstants.*;
import static com.ssb.transactionparser.util.JabaConstants.DATE_FORMAT_MMDDYYYY;
import static com.ssb.transactionparser.util.StatementUtils.*;

@Component
public class ChaseCheckingParser implements TransactionParser {
    private final Logger logger = LoggerFactory.getLogger(ChaseCheckingParser.class);

    @Autowired
    private NotionClientWrapper notionClientWrapper;

    @Override
    public String syncTransactions() {
        logger.info("Syncing ChaseChecking Statements");
        try {
            File directory = new File(System.getenv("STATEMENTS_PATH") + "ChaseChecking");

            for (File file : Objects.requireNonNull(directory.listFiles())) {
                try {
                    String[] HEADERS = {CHASE_CHK_HEADER_DETAILS, CHASE_CHK_HEADER_POSTING_DATE, CHASE_CHK_HEADER_DESCRIPTION,
                            CHASE_CHK_HEADER_AMOUNT, CHASE_CHK_HEADER_BALANCE, CHASE_CHK_HEADER_TYPE, CHASE_CHK_HEADER_INFO};

                    Reader in = new FileReader(file.getAbsolutePath());
                    Iterable<CSVRecord> records = CSVFormat.DEFAULT
                            .builder()
                            .setHeader(HEADERS)
                            .setSkipHeaderRecord(true)
                            .build()
                            .parse(in);

                    for (CSVRecord record : records) {
                        String dateStr = record.get(CHASE_CHK_HEADER_POSTING_DATE);
                        String amountStr = record.get(CHASE_CHK_HEADER_AMOUNT);
                        String description = record.get(CHASE_CHK_HEADER_DESCRIPTION);
                        String type = record.get(CHASE_CHK_HEADER_TYPE);
                        //String details = record.get(CHASE_CHK_HEADER_DETAILS);

                        if (!isValidDate(dateStr, DATE_FORMAT_MMDDYYYY) || !isValidAmount(amountStr)) {
                            logger.error("Not a valid transaction date/amount, hence skipping the transaction");
                            continue;
                        }

                        //Skip payments/other account transfers for now
                        if (StringUtils.equalsIgnoreCase(type, "ACCT_XFER") || isInvalidChaseCheckingDebitTransaction(description)) {
                            logger.info("This is a credit payment/investment transaction, hence skipping");
                            continue;
                        }

                        Date transactionDate = new SimpleDateFormat(DATE_FORMAT_MMDDYYYY).parse(dateStr);
                        float transactionAmount = Float.parseFloat(amountStr);
                        String transactionCategory = findCategoryFromDescription(description); //TODO: Change this to extract category from description
                        String transactionAccount = Account.CHASE_CHECKING.getDescription();

                        notionClientWrapper.writeToNotion(new Transaction(description, transactionCategory, transactionAccount,
                                transactionDate, transactionAmount));
                    }
                } catch (Exception e) {
                    logger.error("Failed to parse file", e);
                    return "Failed to parse file";
                }
            }
        } catch (Exception e) {
            logger.error("Error finding ChaseChecking directory", e);
            return "Error finding ChaseChecking directory";
        }
        logger.info("Successfully synced ChaseChecking Statements");
        return StringUtils.EMPTY;
    }
}
