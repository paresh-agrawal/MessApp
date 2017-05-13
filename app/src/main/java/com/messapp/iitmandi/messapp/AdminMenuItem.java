package com.messapp.iitmandi.messapp;

/**
 * Created by root on 23/3/17.
 */

public class AdminMenuItem {

    private String mess_item;

    public AdminMenuItem(String mess_item){
        this.mess_item = mess_item;
    }

    public String getMessItem() {
        return mess_item;
    }

    public void setMess_item(String mess_item) {
        this.mess_item = mess_item;
    }
}
