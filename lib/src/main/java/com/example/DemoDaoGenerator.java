package com.example;

import java.lang.Exception;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;


public class DemoDaoGenerator {
    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.example.pein.demo.dao");
        createTable(schema);
        generateDaoFiles(schema);
    }

    private static void createTable(Schema schema) {
        Entity story = schema.addEntity("STORY");

        story.addIdProperty();
        story.addStringProperty("storyId").notNull();
        story.addStringProperty("title").notNull();
        story.addStringProperty("images");
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
