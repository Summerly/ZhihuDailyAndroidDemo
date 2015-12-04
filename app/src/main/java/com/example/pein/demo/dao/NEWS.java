package com.example.pein.demo.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "NEWS".
 */
public class NEWS {

    private Long id;
    /** Not-null value. */
    private String newsId;
    private String image;
    private String title;
    private String imageSource;
    private String body;
    private String shareURL;

    public NEWS() {
    }

    public NEWS(Long id) {
        this.id = id;
    }

    public NEWS(Long id, String newsId, String image, String title, String imageSource, String body, String shareURL) {
        this.id = id;
        this.newsId = newsId;
        this.image = image;
        this.title = title;
        this.imageSource = imageSource;
        this.body = body;
        this.shareURL = shareURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getNewsId() {
        return newsId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getShareURL() {
        return shareURL;
    }

    public void setShareURL(String shareURL) {
        this.shareURL = shareURL;
    }

}
