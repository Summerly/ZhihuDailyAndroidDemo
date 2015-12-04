package com.example;

import java.lang.Exception;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;


public class DemoDaoGenerator {
    public static void main(String[] args) {
        Schema schema = new Schema(2, "com.example.pein.demo.dao");
        createTableStory(schema);
        createTableNews(schema);
        generateDaoFiles(schema);
    }

    private static void createTableStory(Schema schema) {
        Entity story = schema.addEntity("STORY");

        story.addIdProperty();
        story.addStringProperty("storyId");
        story.addStringProperty("title").notNull();
        story.addStringProperty("images");
        story.addBooleanProperty("topStories").notNull();
        story.addStringProperty("date");
    }

    private static void createTableNews(Schema schema) {
        Entity news = schema.addEntity("NEWS");

        news.addIdProperty();
        news.addStringProperty("newsId").notNull().unique();
        news.addStringProperty("image");
        news.addStringProperty("title");
        news.addStringProperty("imageSource");
        news.addStringProperty("body");
        news.addStringProperty("shareURL");
    }


    private static void generateDaoFiles(Schema schema) {
        try {
            DaoGenerator generator = new DaoGenerator();
            generator.generateAll(schema, "../ZhihuDailyAndroidDemo/app/src/main/java/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
