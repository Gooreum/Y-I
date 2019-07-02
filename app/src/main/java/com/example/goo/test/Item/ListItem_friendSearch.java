package com.example.goo.test.Item;

/**
 * Created by Goo on 2018-05-04.
 */

//친구검색에 필요한 아이템
public class ListItem_friendSearch {

    //사용자 닉네임
    public String username;
    public String email;
    public boolean btn_follow ;
    //public String btn_unfollow = "팔로우 취소";
    public String url;
    //기본생성자
    public ListItem_friendSearch(){

    }

    //닉네임과 이메일을 매개변수로 하는 생성자
    public ListItem_friendSearch(String username, String email){
        this.username = username;
        this.email = email;
    }

    //사용자 이름을 가져오는 메서드
    public String username(){
        return username;
    }

    //이메일 값을 가져오는 메서드
    public String email(){
        return email;
    }

    public boolean getBtn_follow(){
        return btn_follow;
    }

//    public String getBtn_unfollow(){
//        return btn_unfollow;
//    }
}
