package com.messapp.iitmandi.messapp;

/**
 * Created by root on 23/3/17.
 */

public class AdminFeed {

    private String userEmail;
    private String itemName;
    private String feedText;
    private String rating;
    private String meal;

    public AdminFeed(String userEmail, String meal, String itemName, String rating, String feedText){
        this.userEmail = userEmail;
        this.itemName = itemName;
        this.feedText = feedText;
        this.rating = rating;
        this.meal = meal;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getFeedText() {
        return feedText;
    }

    public void setFeedText(String feedText) {
        this.feedText = feedText;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }
}
