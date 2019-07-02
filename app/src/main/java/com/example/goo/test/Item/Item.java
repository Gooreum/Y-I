package com.example.goo.test.Item;

import java.util.ArrayList;

/**
 * Created by Goo on 2018-05-17.
 */

public class Item extends ArrayList<Item> {

    private String text,subText;
    public String url;
    private boolean isExpandable;
    public String message;
    public int re_reply_count;
    public int reply_id;

    public boolean open ;
    public String sub_username;
    public String sub_url;
    public String sub_message;
    public Item(){}

    public Item(String text, String url, String message, int re_reply_count,int reply_id){
        this.text = text;
        this.url = url;
        this.message = message;
        this.re_reply_count = re_reply_count;
        this.reply_id = reply_id;
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
