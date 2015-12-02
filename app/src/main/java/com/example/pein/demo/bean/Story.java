package com.example.pein.demo.bean;

/**
 * Created by Pein on 15/12/1.
 */
public class Story {
    private String id;
    private String title;
    private String images;

    public Story(String id, String title, String images) {
        this.id = id;
        this.title = title;
        this.images = images;
    }

    public Story() {
        this("", "", "");
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

    public void setImages(String images) {
        this.images = images;
    }

    public String getImages() {
        return this.images;
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", this.id, this.title, this.images);
    }
}
