package com.messapp.iitmandi.messapp;

/**
 * Created by root on 23/3/17.
 */

public class AdminOnLeaveGetter {

    private String userEmail;
    private String toDate;
    private String fromDate;
    private String reason;

    public AdminOnLeaveGetter(String userEmail, String todate, String reason, String fromDate){
        this.userEmail = userEmail;
        this.toDate = todate;
        this.fromDate = fromDate;
        this.reason = reason;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
