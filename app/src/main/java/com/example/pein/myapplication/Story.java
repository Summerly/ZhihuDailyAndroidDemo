package com.example.pein.myapplication;

/**
 * Created by Pein on 15/12/1.
 */
public class Story {
    private String id;
    private String title;

    Story(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", this.id, this.title);
    }
}
