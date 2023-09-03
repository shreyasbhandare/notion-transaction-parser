package com.ssb.transactionparser.parser;

import com.ssb.transactionparser.model.Transaction;
import com.ssb.transactionparser.notion.NotionClientWrapper;
import com.ssb.transactionparser.util.JabaConstants;
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

import static com.ssb.transactionparser.util.JabaConstants.DATE_FORMAT_MMDDYYYY;
import static com.ssb.transactionparser.util.StatementUtils.isValidAmount;
import static com.ssb.transactionparser.util.StatementUtils.isValidDate;

@Component
public class MacysParser implements TransactionParser {
    private final Logger logger = LoggerFactory.getLogger(MacysParser.class);

    @Autowired
    private NotionClientWrapper notionClientWrapper;

    @Override
    public String syncTransactions() {
        logger.info("Syncing MacysCredit Statements");
        try {
            File directory = new File(System.getenv("STATEMENTS_PATH") + "MacysCredit");

            for (File file : Objects.requireNonNull(directory.listFiles())) {
                try {
                    Reader in = new FileReader(file.getAbsolutePath());
                    Iterable<CSVRecord> records = CSVFormat.TDF.parse(in);

                    for (CSVRecord record : records) {
                        String dateStr = record.get(0);
                        String amountStr = record.get(1).replaceAll("[^\\d.]", "");
                        String description = record.get(2);
                        String type = record.get(3);

                        if(StringUtils.equalsIgnoreCase(type, "payment") || StringUtils.equalsIgnoreCase(type, "interest charged")) {
                            logger.error("This is a payment/interest charged transaction, hence skipping the transaction");
                            continue;
                        }

                        if (!isValidDate(dateStr, DATE_FORMAT_MMDDYYYY) || !isValidAmount(amountStr)) {
                            logger.error("Not a valid transaction date/amount, hence skipping the transaction");
                            continue;
                        }

                        Date transactionDate = new SimpleDateFormat(DATE_FORMAT_MMDDYYYY).parse(dateStr);
                        float transactionAmount = Float.parseFloat(amountStr) * (-1);
                        String transactionCategory = JabaConstants.Category.CLOTHES.getDescription(); //TODO: Change this to extract category from description
                        String transactionAccount = JabaConstants.Account.MACYS.getDescription();

                        notionClientWrapper.writeToNotion(new Transaction(description, transactionCategory, transactionAccount,
                                transactionDate, transactionAmount));
                    }
                } catch (Exception e) {
                    logger.error("Failed to parse file", e);
                    return "Failed to parse file";
                }
            }
        } catch (Exception e) {
            logger.error("Error finding MacysCredit directory", e);
            return "Error finding MacysCredit directory";
        }
        logger.info("Successfully synced MacysCredit Statements");
        return StringUtils.EMPTY;
    }
}
