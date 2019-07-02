package com.example.goo.test.Activity.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Activity.AddProjectActivity;
import com.example.goo.test.Activity.Home.Content_Detail_Activity;
import com.example.goo.test.Activity.MainActivity;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Chat_Added_Friend;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Chat_Friend;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Chat_Inviting_Friend;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Join_Member_Detail;
import com.example.goo.test.Fragment.Home.HomeFragment;
import com.example.goo.test.Item.Item_Chat_MyProfile;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Goo on 2018-06-12.
 * 채팅방에 친구 초대를 할 수 있는 화면이다. 친구를 선택할 때 마다 누가 선택 되었는지 툴바 밑에 화면에서 볼 수 있다. 스크롤이 내려가거나 올라가면서
 * 누구를 선택했는지 기억 못하는 경우도 생길 수 있기 때문이다.
 * 친구를 선택하게 되면 arraylist 안에 선택된 친구들을 담는다.
 * 초대 버튼을 누르게 되면 arraylist안에 들어가 있는 친구들을 서버에 보내준다.
 * 서버는 그 친구들의 방을 생성해준다.
 */

public  class  InvitingChatFriendListActivity extends AppCompatActivity implements View.OnClickListener {

    private final String GET_CHAT_FRIEND_LIST = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_chat_friend_list.php";
    private final String ADD_ROOM_NUM = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/add_chat_member.php";
    Toolbar toolbar;
    //초대버튼
    public static Button btn_invite;
    //친구목록을 불러줄 리사이클러뷰
    public  RecyclerView recyclerview_chat_inviting_friend_list;
    static RecyclerView recyclerview_added_friend;
    //각각의 리사이클러뷰에 필요한 아이템 클래스


    public  ArrayList<Item_Chat_MyProfile> listItem_inviting_friend;
    public static  ArrayList<Item_Chat_MyProfile> listItem_added_friend;

    RecyclerViewAdapter_Chat_Added_Friend adapter;
    RecyclerView.LayoutManager layoutManager;

    Intent intent;
    int room_num ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_inviting_friend_list);

        //선택된 친구와 친구목록을 보여줄 리사이클러뷰 객체 생성
        recyclerview_chat_inviting_friend_list = findViewById(R.id.recyclerview_chat_inviting_friend_list);
        recyclerview_added_friend = findViewById(R.id.recyclerview_added_friend);

        //친구목록 어레이리스트 객체 생성
        listItem_inviting_friend = new ArrayList<>();
        listItem_added_friend = new ArrayList<>();




        //툴바
        toolbar = findViewById(R.id.toolBar_info);
        toolbar.setTitle("친구 초대");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                         }
        });
        //초대버튼
        btn_invite = findViewById(R.id.btn_invite);
        //초대인원이 하나도 없으면 초대 버튼을 숨기고 한명이라도 추가 되면 보이도록 한다.
        System.out.println("추가된 인원 수는 " + listItem_added_friend.size());
        if(listItem_added_friend.size() == 0){
            btn_invite.setVisibility(View.GONE);
        }else{
            btn_invite.setVisibility(View.VISIBLE);
        }
        btn_invite.setOnClickListener(this);

        //방번호 가져오기
        intent = getIntent();
        room_num = intent.getIntExtra("room_num",0);
        //친구목록 불러오기
        getFriend();
        getAddedFriend();
    }


    //선택된 친구목록 불러오기
    public void getAddedFriend(){

        RecyclerViewAdapter_Chat_Added_Friend  adapter = new RecyclerViewAdapter_Chat_Added_Friend(InvitingChatFriendListActivity.this, listItem_added_friend);
        LinearLayoutManager layoutManager = new LinearLayoutManager(InvitingChatFriendListActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerview_added_friend.setLayoutManager(layoutManager);
        recyclerview_added_friend.setAdapter(adapter);
        //nestedscrollview가 매끄럽게 움직이게 해줌.
        recyclerview_added_friend.setNestedScrollingEnabled(false);


    }


    //친구 목록 불러오기
    public void getFriend(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_CHAT_FRIEND_LIST ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("my_friend");
                            // JSONObject data = jsonarray.getJSONObject(0);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_Chat_MyProfile item = new Item_Chat_MyProfile();

                                item.username = object.getString("username");
                                item.url = object.getString("profile");
                                item.state_message = object.getString("state_message");
                                item.email = object.getString("email");
                                item.checkbox = false;

                                if(object.getString("profile").equals("null")){
                                    item.url = "http://13.125.216.157/uploads/666.jpg";

                                }


                                listItem_inviting_friend.add(item);



                            }

                            RecyclerViewAdapter_Chat_Inviting_Friend adapter = new RecyclerViewAdapter_Chat_Inviting_Friend(InvitingChatFriendListActivity.this, listItem_inviting_friend);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(InvitingChatFriendListActivity.this);
                            recyclerview_chat_inviting_friend_list.setLayoutManager(layoutManager);
                            recyclerview_chat_inviting_friend_list.setAdapter(adapter);
                            recyclerview_chat_inviting_friend_list.setNestedScrollingEnabled(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InvitingChatFriendListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email_for_friend", loadLoginEmail());


                return params;

            }
        };

        MySingleton.getInstance(InvitingChatFriendListActivity.this).addToRequestque(stringRequest);


    }

    //초대한 친구 서버로 보내서 그 친구들 방목록에 방생성하기
    public void uploadFriendRoom() {
        System.out.println("초대인원은 : " + listItem_added_friend.size());

        //이미지 따로 보내기
        for (int i = 0; i< listItem_added_friend.size();i++) {


            int finalI = i;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_ROOM_NUM,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                // Toast.makeText(AddProjectActivity.this, response, Toast.LENGTH_SHORT).show();


                                //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                                //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                                if ( response.contains("1")) {
                                    //Intent intent = new Intent(InvitingChatFriendListActivity.this, MainActivity.class);
                                   // startActivity(intent);
                                   // Toast.makeText(InvitingChatFriendListActivity.this, "초대 되었습니다.", Toast.LENGTH_LONG).show();

                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(InvitingChatFriendListActivity.this, "Error while uploading image", Toast.LENGTH_LONG).show();
                            }


                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                        System.out.println( listItem_added_friend.get(finalI).username);
                        System.out.println(listItem_added_friend.get(finalI).email);
                        System.out.println(listItem_added_friend.get(finalI).url);
                        System.out.println(String.valueOf(room_num));
                        params.put("username", listItem_added_friend.get(finalI).username);
                        params.put("email", listItem_added_friend.get(finalI).email);
                        params.put("url", listItem_added_friend.get(finalI).url);
                        params.put("added_room_num", String.valueOf(room_num));


                        return params;

                    }
                };



            MySingleton.getInstance(InvitingChatFriendListActivity.this).addToRequestque(stringRequest);
            if(finalI == listItem_added_friend.size()-1 ){
                finish();
            }
        }
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);


        return email_from_login;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_invite:
                uploadFriendRoom();
                break;
        }
    }
}