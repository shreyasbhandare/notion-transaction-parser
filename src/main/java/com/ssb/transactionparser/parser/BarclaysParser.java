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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.ssb.transactionparser.util.CategoryFinderUtil.findCategoryFromDescription;
import static com.ssb.transactionparser.util.JabaConstants.*;
import static com.ssb.transactionparser.util.JabaConstants.DATE_FORMAT_MMDDYYYY;
import static com.ssb.transactionparser.util.StatementUtils.isValidAmount;
import static com.ssb.transactionparser.util.StatementUtils.isValidDate;

@Component
public class BarclaysParser implements TransactionParser {
    private final Logger logger = LoggerFactory.getLogger(BarclaysParser.class);

    @Autowired
    private NotionClientWrapper notionClientWrapper;

    @Override
    public String syncTransactions() {
        logger.info("Syncing BarclaysCredit Statements");
        List<Transaction> transactionList = new ArrayList<>();
        try {
            File directory = new File(System.getenv("STATEMENTS_PATH") + "BarclaysCredit");

            for (File file : Objects.requireNonNull(directory.listFiles())) {
                try {
                    String[] HEADERS = {BARCLAYS_HEADER_TRANSACTION_DATE, BARCLAYS_HEADER_DESCRIPTION,
                            BARCLAYS_HEADER_CATEGORY, BARCLAYS_HEADER_AMOUNT};

                    Reader in = new FileReader(file.getAbsolutePath());
                    Iterable<CSVRecord> records = CSVFormat.DEFAULT
                            .builder()
                            .setHeader(HEADERS)
                            .setSkipHeaderRecord(true)
                            .build()
                            .parse(in);

                    for (CSVRecord record : records) {
                        String dateStr = record.get(BARCLAYS_HEADER_TRANSACTION_DATE);
                        String amountStr = record.get(BARCLAYS_HEADER_AMOUNT);
                        String description = record.get(BARCLAYS_HEADER_DESCRIPTION);

                        if(StringUtils.containsIgnoreCase(description, "Payment Received")) {
                            logger.info("This is a payment transaction, hence skipping the transaction");
                            continue;
                        }

                        if (!isValidDate(dateStr, DATE_FORMAT_MMDDYYYY) || !isValidAmount(amountStr)) {
                            logger.error("Not a valid transaction date/amount, hence skipping the transaction");
                            continue;
                        }

                        Date transactionDate = new SimpleDateFormat(DATE_FORMAT_MMDDYYYY).parse(dateStr);
                        float transactionAmount = Float.parseFloat(amountStr);
                        String transactionCategory = findCategoryFromDescription(description); //TODO: Change this to extract category from description
                        String transactionAccount = Account.BARCLAYS.getDescription();

                        notionClientWrapper.writeToNotion(new Transaction(description, transactionCategory, transactionAccount,
                                transactionDate, transactionAmount));
                    }
                } catch (Exception e) {
                    logger.error("Failed to parse file", e);
                    return "Failed to parse file";
                }
            }
        } catch (Exception e) {
            logger.error("Error finding BarclaysCredit directory", e);
            return "Error finding BarclaysCredit directory";
        }
        logger.info("Successfully synced BarclaysCredit Statements");
        return StringUtils.EMPTY;
    }
}
