package com.ssb.transactionparser.notion;

import com.ssb.transactionparser.model.Transaction;
import com.ssb.transactionparser.util.JabaConstants;
import jakarta.annotation.PostConstruct;
import notion.api.v1.NotionClient;
import notion.api.v1.model.common.PropertyType;
import notion.api.v1.model.common.RichTextType;
import notion.api.v1.model.databases.Database;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageParent;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.model.databases.DatabaseProperty.Select.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class NotionClientWrapper {

    private static final Logger logger = LoggerFactory.getLogger(NotionClientWrapper.class);
    private static final String NOTION_DATABASE_ID = System.getenv("NOTION_DATABASE_ID");
    private static final String NOTION_TOKEN = System.getenv("NOTION_TOKEN");

    private NotionClient notionClient;
    private Map<String, Option> typeMap;
    private Map<String, Option> categoryMap;

    @PostConstruct
    public void initialize() {
        notionClient = new NotionClient(NOTION_TOKEN);
        Database db = notionClient.retrieveDatabase(NOTION_DATABASE_ID);

        List<Option> typeList = Objects.requireNonNull(db.getProperties().get("Type").getSelect()).getOptions();
        if(typeList != null && !typeList.isEmpty()) {
            typeMap = new HashMap<>();
            typeList.forEach(option -> typeMap.put(option.getName(), option));
        }

        List<Option> categoryOptions = Objects.requireNonNull(db.getProperties().get("Category").getSelect()).getOptions();
        if(categoryOptions != null && !categoryOptions.isEmpty()) {
            categoryMap = new HashMap<>();
            categoryOptions.forEach(option -> categoryMap.put(option.getName(), option));
        }
    }

    public void writeToNotion(Transaction transaction) {
        try {
            logger.info("Saving the transaction : " + transaction);
            Page page = notionClient.createPage(
                    PageParent.database(NOTION_DATABASE_ID),
                    Map.of("Account", new PageProperty("1", PropertyType.RichText, null, Arrays.asList(new PageProperty.RichText(RichTextType.Text, new PageProperty.RichText.Text(transaction.getAccount())))),
                            "Date", new PageProperty("2", PropertyType.Date, null, null, null, null, null, null, new PageProperty.Date(new SimpleDateFormat(JabaConstants.DATE_FORMAT_YYYY_MM_DD).format(transaction.getDate()))),
                            "Amount", new PageProperty("3", PropertyType.Number, null, null, null, null, null, transaction.getAmount()),
                            "Type", new PageProperty("4", PropertyType.Select, null, null, typeMap.get(transaction.getType())),
                            "Category", new PageProperty("5", PropertyType.Select, null, null, categoryMap.get(transaction.getCategory())),
                            "Description", new PageProperty("6", PropertyType.Title, Arrays.asList(new PageProperty.RichText(RichTextType.Text, new PageProperty.RichText.Text(transaction.getDescription()))))),
                    null,
                    null,
                    null);
            logger.info("Saved the transaction as Notion page : " + page);
        } catch (Exception e) {
            logger.error(String.format("Error saving transaction to Notion : %s", transaction), e);
        }
    }
}

