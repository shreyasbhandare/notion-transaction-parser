package com.ssb.transactionparser.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static com.ssb.transactionparser.util.JabaConstants.*;

public class StatementUtils {
    public static boolean isValidDate(String dateStr, String format) {
        try {
            new SimpleDateFormat(format).parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidAmount(String amountStr) {
        try {
            Float.parseFloat(amountStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int getYear(String transactDate, Date endDate, Account accountType) {
        LocalDate localDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        switch (accountType) {
            case CHASE_CREDIT:
            case CHASE_CHECKING:
                String[] mmdd = transactDate.split(FWSLASH);
                if (Integer.parseInt(mmdd[0]) > localDate.getMonthValue()) {
                    return localDate.getYear() - 1;
                }
                break;
            case CAPITAL_ONE:
                String[] mmm_dd = transactDate.split(WHITESPACE);
                if (Month.valueOf(monthMap.get(mmm_dd[0].toUpperCase())).getValue() > localDate.getMonthValue()) {
                    return localDate.getYear() - 1;
                }
                break;
            default:
                break;
        }
        return localDate.getYear();
    }

    public static void writeToFile(String fileName, String text) {
        // **** Note that pW must be declared before the try block
        PrintWriter pW = null;
        try {
            pW = new PrintWriter(fileName);
            pW.write(text);
        } catch (IOException e) {
            e.printStackTrace();  // *** this is more informative ***
        } finally {
            if (pW != null) {
                pW.close(); // **** closing it flushes it and reclaims resources ****
            }
        }
    }

    public static String[] getTransactionsFromText(String text) {
        String[] lines = text.split(NEWLINE);
        StringBuilder transactionBuilder = new StringBuilder();

        for (String line : lines) {
            String[] transactionComponents = line.split(WHITESPACE);

            if (transactionComponents.length > 2 && isValidDate(transactionComponents[0], DATE_FORMAT_MMDD)
                    && isValidAmount(transactionComponents[transactionComponents.length - 1].replaceAll("[^\\d.-]", ""))) {
                transactionBuilder.append(line).append("\n");
            }
        }

        return transactionBuilder.toString().split(NEWLINE);
    }

    public static boolean isInvalidChaseCheckingDebitTransaction(String line) {
        if (StringUtils.containsIgnoreCase(line, "Discover")
                    || StringUtils.containsIgnoreCase(line, "Chase Card")
                    || StringUtils.containsIgnoreCase(line, "Macys")
                    || StringUtils.containsIgnoreCase(line, "Marcus")
                    || StringUtils.containsIgnoreCase(line, "Capital One")
                    || StringUtils.containsIgnoreCase(line, "BARCLAYCARD")
                    || StringUtils.containsIgnoreCase(line, "Betterment")
                    || StringUtils.containsIgnoreCase(line, "Robinhood")
                    || StringUtils.containsIgnoreCase(line, "1700")
                    || StringUtils.containsIgnoreCase(line, "Fid Bkg")) {
            return true;
        }
        return false;
    }

    public static String[] getStartEndDates(String text) {
        String[] lines = text.split(NEWLINE);

        for(String line : lines) {
            if(line != null) {
                String[] dateArr = line.split("through");
                if(dateArr.length == 2) {
                    String potentialStartDate = dateArr[0].replaceAll("[\\r\\n\\t]", "").trim();
                    String potentialEndDate = dateArr[1].replaceAll("[\\r\\n\\t]", "").trim();

                    if(isValidDate(potentialStartDate, DATE_FORMAT_MMMM_DD_YYYY) && isValidDate(potentialEndDate, DATE_FORMAT_MMMM_DD_YYYY))
                        return new String[]{potentialStartDate, potentialEndDate};
                }
            }
        }

        return new String[]{};
    }
}
