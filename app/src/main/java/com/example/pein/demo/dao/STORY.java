package com.example.pein.demo.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "STORY".
 */
public class STORY {

    private Long id;
    private String storyId;
    /** Not-null value. */
    private String title;
    private String images;
    private boolean topStories;
    private String date;

    public STORY() {
    }

    public STORY(Long id) {
        this.id = id;
    }

    public STORY(Long id, String storyId, String title, String images, boolean topStories, String date) {
        this.id = id;
        this.storyId = storyId;
        this.title = title;
        this.images = images;
        this.topStories = topStories;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    /** Not-null value. */
    public String getTitle() {
        return title;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public boolean getTopStories() {
        return topStories;
    }

    public void setTopStories(boolean topStories) {
        this.topStories = topStories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
