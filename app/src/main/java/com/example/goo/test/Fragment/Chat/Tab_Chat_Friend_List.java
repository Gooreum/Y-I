package com.example.goo.test.Fragment.Chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.example.goo.test.Adapter.RecyclerViewAdapter_Chat_Friend;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Follower;
import com.example.goo.test.Item.Item_Chat_MyProfile;

import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-06-04.
 */

public class Tab_Chat_Friend_List extends Fragment {
    private  final String GET_CHAT_FRIEND = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_chat_friend.php";
    private  final String GET_CHAT_FRIEND_LIST = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_chat_friend_list.php";
    //내 프로필 이미지,이름, 상태메세지 ,친구 숫자 xml 껍데기 만들기
    private CircleImageView prof_pic;
    private TextView username,state_message,friend_count;
    //친구목록을 불러줄 리사이클러뷰
    RecyclerView recyclerview_chat_friend_list,recyclerview_recommend_friend;
    //각각의 리사이클러뷰에 필요한 아이템 클래스
    ArrayList<Item_Chat_MyProfile> listItem_friend;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_chat_friend_list, container, false);

        //내 프로필 이미지,이름, 상태메세지, 친구명수xml 객체 생성
        prof_pic = rootView.findViewById(R.id.prof_pic);
        username = rootView.findViewById(R.id.username);
        state_message = rootView.findViewById(R.id.state_message);
        friend_count = rootView.findViewById(R.id.friend_count);

        //추천친구와 친구목록을 보여줄 리사이클러뷰 객체 생성
        recyclerview_chat_friend_list = rootView.findViewById(R.id.recyclerview_chat_friend_list);
        recyclerview_recommend_friend = rootView.findViewById(R.id.recyclerview_recommend_friend);

        //친구목록 어레이리스트 객체 생성
        listItem_friend = new ArrayList<>();

        //내프로필 불러오기
        uploadMyProfile();

        //친구목록 불러오기
        getFriend();
        return rootView;
    }

    //내프로필 업로드
    public void uploadMyProfile(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_CHAT_FRIEND ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("my_profile");
                            // JSONObject data = jsonarray.getJSONObject(0);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_Chat_MyProfile item = new Item_Chat_MyProfile();
                                System.out.println("유저네임 " + object.getString("username"));
                                item.username = object.getString("username");
                                item.url = object.getString("profile");
                                item.state_message = object.getString("state_message");

                                username.setText(item.username);
                                state_message.setText(item.state_message);
                                Picasso.with(getContext()).load(item.url).error(R.mipmap.ic_launcher_round).into(prof_pic);


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", loadLoginEmail());


                return params;

            }
        };

        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);



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


                                if(object.getString("profile").equals("null")){
                                    item.url = "http://13.125.216.157/uploads/666.jpg";

                                }


                                listItem_friend.add(item);



                            }

                            RecyclerViewAdapter_Chat_Friend adapter = new RecyclerViewAdapter_Chat_Friend(getContext(), listItem_friend);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recyclerview_chat_friend_list.setLayoutManager(layoutManager);
                            recyclerview_chat_friend_list.setAdapter(adapter);
                            recyclerview_chat_friend_list.setNestedScrollingEnabled(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);


    }
    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);


        return email_from_login;
    }

}