package android.example.blogtalk.Models;

import com.google.firebase.database.ServerValue;

public class Post {

    private String title;
    private String description;
    private String pictures;
    private String userId;
    private String userImage;
    private Object timeStamp;
    private String postKey;

    public Post(String title, String description, String pictures, String userId, String userImage) {
        this.title = title;
        this.description = description;
        this.pictures = pictures;
        this.userId = userId;
        this.userImage = userImage;
        this.timeStamp = ServerValue.TIMESTAMP;
    }
    public Post(){

    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPictures() {
        return pictures;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
