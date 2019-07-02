package com.example.goo.test.Fragment.Chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Chat_Friend;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Chat_Room;
import com.example.goo.test.Item.Item_Chat_MyProfile;
import com.example.goo.test.Item.Item_Chat_Room_List;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-06-06.
 */

public class Tab_Chat_Room_List extends Fragment {
    private static final String GET_CHAT_ROOM_LIST = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_chat_room_list.php";

    //채팅방 목록을 보여줄 리사이클러뷰
    RecyclerView recyclerview_chat_room_list;
    //채팅방 목록에 필요한 아이템을 arraylist로.
    ArrayList<Item_Chat_Room_List> listItem_room_list;

    //스와이프 레이아웃
    SwipeRefreshLayout swipe_refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_chat_room_list, container, false);
        //채팅방 목록을 보여줄 리사이클러뷰 객체 생성
        recyclerview_chat_room_list = rootView.findViewById(R.id.recyclerview_chat_room_list);


        //채팅방 목록에 들어갈 아이템 어레이리스트 객체 생성
        listItem_room_list = new ArrayList<>();

        //스와이프 객체 생성
        //스와이프
        swipe_refresh = rootView.findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        //방목록을 불러온다.
        getRoomList();
        return rootView;
    }

    //채팅방 목록 불러오기
    public void getRoomList(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_CHAT_ROOM_LIST ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("room_list");


                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_Chat_Room_List item = new Item_Chat_Room_List();

                                item.room_num = object.getInt("room_id");
                                item.username = object.getString("username");

                                item.history = object.getString("history");
                                item.users = object.getString("users");

                                item.email = object.getString("email");
                                item.url = object.getString("profile_img");
                                item.project_state = object.getInt("project");
                                item.member_count = object.getInt("member_count");
                                item.project_id = object.getInt("project_id");
                                item.boss = object.getString("boss");
                                item.room_name = object.getString("room_name");


                                System.out.println(item.room_num );
                                System.out.println(item.username );
                                System.out.println(item.history );
                                System.out.println(item.email );

                                listItem_room_list.add(item);

                            }

                            RecyclerViewAdapter_Chat_Room adapter = new RecyclerViewAdapter_Chat_Room(getContext(), listItem_room_list);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recyclerview_chat_room_list.setLayoutManager(layoutManager);
                            recyclerview_chat_room_list.setAdapter(adapter);
                            recyclerview_chat_room_list.setNestedScrollingEnabled(false);
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
    public void refresh(){
        //전송 후 프래그먼트를 띄었다가 다시 붙임.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);


        return email_from_login;
    }

    //다른 탭에서 넘어 왔을 때 새로고침을 해줌.
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}
