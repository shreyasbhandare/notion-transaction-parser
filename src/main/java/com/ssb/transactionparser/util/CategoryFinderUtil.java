package com.ssb.transactionparser.util;

import org.apache.commons.lang3.StringUtils;

import static com.ssb.transactionparser.util.JabaConstants.Category;

public class CategoryFinderUtil {
    public static String findCategoryFromDescription(String description) {
        // Senate / Rent
        if(StringUtils.containsIgnoreCase(description, "Senate")) {
            return Category.RENT.getDescription();
        }

        // Lemonade
        if(StringUtils.containsIgnoreCase(description, "Lemonade")) {
            return Category.RENTERS_INS.getDescription();
        }

        // Uber Eats/Chipotle/Cava/Honest/Pizza/Ramen/Noodle/Starbucks/Rajbhog/Papa John/
        if(StringUtils.containsIgnoreCase(description, "Uber Eats")
                || StringUtils.containsIgnoreCase(description, "Chipotle")
                || StringUtils.containsIgnoreCase(description, "Cava")
                || StringUtils.containsIgnoreCase(description, "Honest")
                || StringUtils.containsIgnoreCase(description, "Pizza")
                || StringUtils.containsIgnoreCase(description, "Noodle")
                || StringUtils.containsIgnoreCase(description, "Ramen")
                || StringUtils.containsIgnoreCase(description, "Honest")
                || StringUtils.containsIgnoreCase(description, "Starbucks")
                || StringUtils.containsIgnoreCase(description, "Rajbhog")
                || StringUtils.containsIgnoreCase(description, "Papa John")
                || StringUtils.containsIgnoreCase(description, "Dunkin")
                || StringUtils.containsIgnoreCase(description, "Atul")
                || StringUtils.containsIgnoreCase(description, "Fooda")
                || StringUtils.containsIgnoreCase(description, "EMPYREAN")
                || StringUtils.containsIgnoreCase(description, "Indian Kitchen")) {
            return Category.RESTAURANTS.getDescription();
        }

        // Uber/Lyft
        if(StringUtils.containsIgnoreCase(description, "Uber")
            || StringUtils.containsIgnoreCase(description, "Lyft")) {
            return Category.TRAVEL.getDescription();
        }

        // Fresh/Grocer/Apna/Patel/Bazar
        if(StringUtils.containsIgnoreCase(description, "Grocer")
                || StringUtils.containsIgnoreCase(description, "Apna")
                || StringUtils.containsIgnoreCase(description, "Patel")
                || StringUtils.containsIgnoreCase(description, "Bazar")
                || StringUtils.containsIgnoreCase(description, "Fresh")) {
            return Category.GROCERY.getDescription();
        }

        // Amazon
        if(StringUtils.containsIgnoreCase(description, "Amazon")
                || StringUtils.containsIgnoreCase(description, "Amzn")
                || StringUtils.containsIgnoreCase(description, "Walmart")) {
            return Category.HOME_IMPROVEMENT.getDescription();
        }

        // PSEG
        if(StringUtils.containsIgnoreCase(description, "PSEG")) {
            return Category.PSEG.getDescription();
        }

        // Hulu/Netflix/HBO/Prime/Zee5
        if(StringUtils.containsIgnoreCase(description, "Hulu")
                || StringUtils.containsIgnoreCase(description, "Netflix")
                || StringUtils.containsIgnoreCase(description, "HBO")
                || StringUtils.containsIgnoreCase(description, "MAX")
                || StringUtils.containsIgnoreCase(description, "Prime")
                || StringUtils.containsIgnoreCase(description, "Zee5")
                || StringUtils.containsIgnoreCase(description, "Disney")) {
            return Category.SUBSCRIPTIONS.getDescription();
        }

        // Verizon
        if(StringUtils.containsIgnoreCase(description, "Verizon")) {
            return Category.WIFI.getDescription();
        }

        // Venmo (phone bill)/LYCAMOBILE
        if(StringUtils.containsIgnoreCase(description, "LYCAMOBILE")
        || StringUtils.containsIgnoreCase(description, "VENMO")) {
            return Category.PHONE.getDescription();
        }

        // Loft/JCPenny/Lulu/
        if(StringUtils.containsIgnoreCase(description, "Loft")
                || StringUtils.containsIgnoreCase(description, "JCPenny")
                || StringUtils.containsIgnoreCase(description, "Lulu")
                || StringUtils.containsIgnoreCase(description, "H&M")) {
            return Category.CLOTHES.getDescription();
        }

        // Path/MTA/
        if(StringUtils.containsIgnoreCase(description, "Path")
                || StringUtils.containsIgnoreCase(description, "MTA")
                || StringUtils.containsIgnoreCase(description, "Metrocard")) {
            return Category.TRAVEL.getDescription();
        }

        //Paycheck
        if(StringUtils.containsIgnoreCase(description, "GS service")
                || StringUtils.contains(description, "ADP")
                || StringUtils.containsIgnoreCase(description, "Citi")
                || StringUtils.containsIgnoreCase(description, "TotalSource")
                || StringUtils.containsIgnoreCase(description, "Deposit")
                || StringUtils.containsIgnoreCase(description, "Payroll")) {
            return Category.PAYCHECK.getDescription();
        }

        return Category.MISC.getDescription();
    }
}
