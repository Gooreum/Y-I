package com.example.goo.test.Item;

import java.util.ArrayList;

/**
 * Created by Goo on 2018-05-17.
 */

public class Item2 extends ArrayList<Item2> {

    private String text,subText;
    public String url;
    private boolean isExpandable;
    public String message;

    public String sub_username;
    public String sub_url;
    public String sub_message;
    public Item2(){}
    public Item2(String sub_username, String sub_url, String sub_message){
        this.sub_username = sub_username;
        this.sub_url = sub_url;
        this.sub_message = sub_message;
    }
    public Item2(String text, String sub_username, String sub_url, boolean isExpandable){
        this.text = text;
        this.sub_username = sub_username;
        this.sub_url = sub_url;
        this.isExpandable = isExpandable;
    }
    public Item2(String text, boolean isExpandable){
        this.text = text;

        this.isExpandable = isExpandable;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean isExpandable) {
        this.isExpandable = isExpandable;
    }
}
