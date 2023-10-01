package com.ssb.transactionparser.util;

import java.util.Map;

import static java.util.Map.entry;

public class JabaConstants {
    public enum TYPE {
        DEBIT, CREDIT
    }

    public enum Category {
        RESTAURANTS("Restaurant"),
        GROCERY("Grocery"),
        TRAVEL("Travel"),
        SUBSCRIPTIONS("Subscriptions"),
        CLOTHES("Clothes & Accessories"),
        ELECTRONICS("Electronics"),
        HOME_IMPROVEMENT("Home Improvement"),
        WIFI("WiFi"),
        RENT("Rent"),
        HEALTH("Health"),
        PHONE("Phone"),
        PSEG("Electricity"),
        EDUCATION("Education"),
        MISC("Miscellaneous"),
        MOVIES("Movies"),
        IMMIGRATION("Immigration"),
        RENTERS_INS("Renter's Insurance"),
        PAYCHECK("Paycheck"),
        ENTERTAINMENT("Entertainment"),
        INTERNAL_PAYMENT("Internal Payment");

        private final String desc;

        Category(String desc) {
            this.desc = desc;
        }

        public String getDescription(){
            return desc;
        }
    }

    public enum Account {
        CHASE_CREDIT("Chase Credit"),
        CHASE_CHECKING("Chase Checking"),
        DISCOVER("Discover Credit"),
        CAPITAL_ONE("Capital One Credit"),
        MACYS("Macy's Credit"),
        BARCLAYS("Barclays Credit");

        String desc;

        Account(String desc) {
            this.desc = desc;
        }

        public String getDescription() {
            return this.desc;
        }
    }

    // Date Formats
    public static final String DATE_FORMAT_MMDDYYYY = "MM/dd/yyyy";
    public static final String DATE_FORMAT_MM_DD_YYYY = "MM-dd-yyyy";
    public static final String DATE_FORMAT_DDMMYYYY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_MMDD = "MM/dd";
    public static final String DATE_FORMAT_MMDDYY = "MM/dd/yy";
    public static final String DATE_FORMAT_MMM_DD = "MMM dd";
    public static final String DATE_FORMAT_MMMM_DD_YYYY = "MMMM dd, yyyy";

    // Regex Helpers
    public static final String WHITESPACE = "\\s+";
    public static final String NEWLINE = "\n";
    public static final String HYPHEN = "-";
    public static final String BAR = "\\|";
    public static final String FWSLASH = "/";

    public static final Map<String, String> monthMap = Map.ofEntries(
            entry("JAN", "JANUARY"),
            entry("FEB", "FEBRUARY"),
            entry("MAR", "MARCH"),
            entry("APR", "APRIL"),
            entry("MAY", "MAY"),
            entry("JUN", "JUNE"),
            entry("JUL", "JULY"),
            entry("AUG", "AUGUST"),
            entry("SEP", "SEPTEMBER"),
            entry("OCT", "OCTOBER"),
            entry("NOV", "NOVEMBER"),
            entry("DEC", "DECEMBER"));

    // DiscoverCredit
    public static final String HEADER_TRANS_DATE = "Trans. Date";
    public static final String HEADER_POST_DATE = "Post Date";
    public static final String HEADER_DESCRIPTION = "Description";
    public static final String HEADER_AMOUNT = "Amount";
    public static final String HEADER_CATEGORY = "Category";

    // ChaseCredit
    public static final String CHASE_HEADER_TRANSACTION_DATE = "Transaction Date";
    public static final String CHASE_HEADER_POST_DATE = "Post Date";
    public static final String CHASE_HEADER_DESCRIPTION = "Description";
    public static final String CHASE_HEADER_AMOUNT = "Amount";
    public static final String CHASE_HEADER_CATEGORY = "Category";
    public static final String CHASE_HEADER_TYPE = "Type";
    public static final String CHASE_HEADER_MEMO = "Memo";

    // ChaseChecking
    public static final String CHASE_CHK_HEADER_DETAILS = "Details";
    public static final String CHASE_CHK_HEADER_POSTING_DATE = "Posting Date";
    public static final String CHASE_CHK_HEADER_DESCRIPTION = "Description";
    public static final String CHASE_CHK_HEADER_AMOUNT = "Amount";
    public static final String CHASE_CHK_HEADER_BALANCE = "Balance";
    public static final String CHASE_CHK_HEADER_TYPE = "Type";
    public static final String CHASE_CHK_HEADER_INFO = "Check or Slip #";

    // CapitalOne
    public static final String CAPITAL_ONE_HEADER_TRANSACTION_DATE = "Transaction Date";
    public static final String CAPITAL_ONE_HEADER_POSTED_DATE = "Posted Date";
    public static final String CAPITAL_ONE_HEADER_CARD_NO = "Card No.";
    public static final String CAPITAL_ONE_HEADER_DESCRIPTION = "Description";
    public static final String CAPITAL_ONE_HEADER_CATEGORY = "Category";
    public static final String CAPITAL_ONE_HEADER_DEBIT = "Debit";
    public static final String CAPITAL_ONE_HEADER_CREDIT = "Credit";

    // Barclays
    public static final String BARCLAYS_HEADER_TRANSACTION_DATE = "Transaction Date";
    public static final String BARCLAYS_HEADER_DESCRIPTION = "Description";
    public static final String BARCLAYS_HEADER_CATEGORY = "Category";
    public static final String BARCLAYS_HEADER_AMOUNT = "Amount";
}