package com.example.index.fantastic_app.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "Post")
public class Post {
    @Id
    @GeneratedValue
    private int postId;
    private String authorName;
    private String imageURL;
    private String contentText;
    private LocalDateTime  createdTime;
    private LocalDateTime  updatedTime;
    private Risk risk;

    public Post(int postId, String authorName, String imageURL, String contentText, LocalDateTime createdTime, LocalDateTime updatedTime, Risk risk) {
        this.postId = postId;
        this.authorName = authorName;
        this.imageURL = imageURL;
        this.contentText = contentText;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.risk = risk;
    }

    protected Post() {

    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }
}
