package com.ssb.transactionparser.model;

import com.ssb.transactionparser.util.JabaConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Transaction {
    private float amount;
    private Date date;
    private String description;
    private String category;
    private String type;
    private String account;

    public Transaction(String description, String category, String account, Date date, float amount) {
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.account = account;

        if(amount < 0) {
            this.type = JabaConstants.TYPE.DEBIT.toString();
        } else {
            this.type = JabaConstants.TYPE.CREDIT.toString();
        }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "account=" + account +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
