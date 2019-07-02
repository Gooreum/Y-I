package com.example.goo.test.Item;

import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Goo on 2018-05-07.
 * 프로젝트 작성글에 필요한 아이템들을 모아둔 클래스
 */

public class ListItem_Show_Project {
    //public ArrayList<String> project_url;

    public int id;
    public String email;
    public String username;
    public String title;
    public String content;
    public String history;
    public boolean like;
    public boolean join;
    public boolean me ;
    public boolean counting;
    public String url;
    public String apply_duration;
    public String dev_duration;
    public String gps;
    public int member_num;

    public int like_count;
    public int join_count;
    public int reply_count;
    public int selected_num;
    public int img_count;
    public boolean isImage ;

    public boolean scrollview_pic;
    public boolean linear_pic;

    public boolean img1;
    public boolean img2;
    public boolean img3;
    public boolean img4;
    public boolean img5;

    public String img_url1;
    public String img_url2;
    public String img_url3;
    public String img_url4;
    public String img_url5;

    public String text1;
    public String text2;
    public String text3;
    public String text4;
    public String text5;

    public int project_recruting;
    public int project_developing;
    public int more_recruit_member;
    public int project_completed;
    public String project_url;
    public int amount;



    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getHistory() {
        return history;
    }
    public int getReply_count() {
        return reply_count;
    }

    public boolean getLike() {
        return like;
    }

    public boolean getJoin() {
        return join;
    }
    public boolean getMe() {
        return me;
    }


    public int getLike_count() {
        return like_count;
    }
    public int getJoin_count() {
        return join_count;
    }

}
