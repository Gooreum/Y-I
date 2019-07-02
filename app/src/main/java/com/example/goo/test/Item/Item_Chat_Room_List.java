package com.example.goo.test.Item;

/**
 * Created by Goo on 2018-06-06.
 */

public class Item_Chat_Room_List {
    public String username;
    public String username2;
    public String email;
    public String email2;
    public String history;
    public int room_num;
    public String users;
    public String url;
    public String message;
    public int me;
    public int none;
    public String img;
    public String link_url;
    public int img_in ;
    public int message_in ;
    public int link_url_in ;
    public int project_id ;
    public String room_name ;

    public int project_state;
    public int member_count;
    public String boss;

    public Item_Chat_Room_List(){

    }

    public Item_Chat_Room_List(int room_num,String username,String url ,String message,String img,String history){
        this.room_num = room_num;
        this.username = username;
        this.url = url;
        this.message = message;
        this.img = img;
        this.history = history;
    }

    public Item_Chat_Room_List(int room_num,String username,String url ,String message,String img,String history,int me,int none,int img_in,int message_in,int link_url_in){
        this.room_num = room_num;
        this.username = username;
        this.url = url;
        this.message = message;
        this.img = img;
        this.history = history;
        this.me = me;
        this.none = none;
        this.img_in = img_in;
        this.message_in = message_in;
        this.link_url_in = link_url_in;
    }
}
