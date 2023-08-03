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
import static com.ssb.transactionparser.util.JabaConstants.DATE_FORMAT_YYYY_MM_DD;
import static com.ssb.transactionparser.util.StatementUtils.isValidAmount;
import static com.ssb.transactionparser.util.StatementUtils.isValidDate;

@Component
public class CapitalOneParser implements TransactionParser {
    private final Logger logger = LoggerFactory.getLogger(CapitalOneParser.class);

    @Autowired
    private NotionClientWrapper notionClientWrapper;

    @Override
    public String syncTransactions() {
        logger.info("Syncing CapitalOne Statements");
        try {
            File directory = new File(System.getenv("STATEMENTS_PATH") + "CapitalOneCredit");

            for (File file : Objects.requireNonNull(directory.listFiles())) {
                try {
                    String[] HEADERS = {CAPITAL_ONE_HEADER_TRANSACTION_DATE, CAPITAL_ONE_HEADER_POSTED_DATE,
                            CAPITAL_ONE_HEADER_CARD_NO, CAPITAL_ONE_HEADER_DESCRIPTION, CAPITAL_ONE_HEADER_CATEGORY,
                            CAPITAL_ONE_HEADER_DEBIT, CAPITAL_ONE_HEADER_CREDIT};

                    Reader in = new FileReader(file.getAbsolutePath());
                    Iterable<CSVRecord> records = CSVFormat.DEFAULT
                            .builder()
                            .setHeader(HEADERS)
                            .setSkipHeaderRecord(true)
                            .build()
                            .parse(in);

                    for (CSVRecord record : records) {
                        String dateStr = record.get(CAPITAL_ONE_HEADER_TRANSACTION_DATE);
                        String amountStr = isValidAmount(record.get(CAPITAL_ONE_HEADER_DEBIT))
                                ? "-" + record.get(CAPITAL_ONE_HEADER_DEBIT)
                                : record.get(CAPITAL_ONE_HEADER_CREDIT);
                        String description = record.get(CAPITAL_ONE_HEADER_DESCRIPTION);
                        //TODO String category = record.get(CAPITAL_ONE_HEADER_CATEGORY);

                        if(StringUtils.containsIgnoreCase(description, "CAPITAL ONE AUTOPAY PYMT")) {
                            logger.error("This is a payment transaction, hence skipping the transaction");
                            continue;
                        }

                        if (!isValidDate(dateStr, DATE_FORMAT_YYYY_MM_DD) || !isValidAmount(amountStr)) {
                            logger.error("Not a valid transaction date/amount, hence skipping the transaction");
                            continue;
                        }

                        Date transactionDate = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD).parse(dateStr);
                        float transactionAmount = Float.parseFloat(amountStr);
                        String transactionCategory = findCategoryFromDescription(description); //TODO: Change this to extract category from description
                        String transactionAccount = Account.CAPITAL_ONE.getDescription();

                        notionClientWrapper.writeToNotion(new Transaction(description, transactionCategory, transactionAccount,
                                transactionDate, transactionAmount));
                    }
                } catch (Exception e) {
                    logger.error("Failed to parse file", e);
                    return "Failed to parse file";
                }
            }
        } catch (Exception e) {
            logger.error("Error finding CapitalOne directory", e);
            return "Error finding CapitalOne directory";
        }
        logger.info("Successfully synced CapitalOne Statements");
        return StringUtils.EMPTY;
    }
}
