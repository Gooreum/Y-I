package com.example.goo.test.Activity.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Activity.Chat.ChatProfileActivity;
import com.example.goo.test.Activity.Chat.ChatRoomActivity;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Join_Member;

import com.example.goo.test.Fragment.Home.HomeFragment;
import com.example.goo.test.Item.ListItem_Check_Join_Member;

import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Goo on 2018-05-11.
 * 프로젝트 모집글에 신청한 인원의 명단을 보여주는 프래그먼트.
 */

public class Check_Join_Member extends AppCompatActivity implements View.OnClickListener {
    private static final String GET_CHAT_ROOM_NUM = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_chat_room_num.php";
    public static int project_id;
    String project_username;
    public static String project_email;
    //join_count는 작성자가 작성한 모집인원수
    public static int join_count;
    //selected_count는 선택된 모집인원수.
    public static int selected_count;
    public static Button btn_start;
    private TextView txt_member_num;
    public static TextView txt_selected_num;
    //static을 붙인 이유는 위에 있는 변수나 뷰들이 프래그먼트에 붙어 있는 것들인데,
    // 이것들이 나중에 리사
    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private android.support.v7.widget.Toolbar toolbar;

    private static final String SHOW_JOIN_MEMBER = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/show_check_join_member.php";
    private static final String ADD_CHAT_ROOM_FROM_PROJECT = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/add_chat_room_from_project.php";
    public static final String UPDATE_PROJECT_STATE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/update_project_content_state.php";

    List<ListItem_Check_Join_Member> listItems;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_check_join_member);


        //툴바
        toolbar = findViewById(R.id.toolbar);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("신청인원");
        toolbar.setTitleTextColor(0xFFFFFFFF);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


                getSupportFragmentManager().beginTransaction().replace(R.id.frame_join, new HomeFragment()).commit();

            }
        });

        //btn_start는 모집글 작성자가 모집인원 이상의 사람들을 선택했을 때 프로젝트를 시작할 수 있또록 해주는 버튼이다.
        btn_start = findViewById(R.id.btn_start);

        //txt_member_num은 '모집인원 수'를 나태내주는 텍스트뷰이다.
        txt_member_num = findViewById(R.id.txt_member_num);
        //txt_selected_num은 '함께하기' 버튼을 누른 사람들의 수를 서버에 저장해두었다가 불러 올 때 입력하는 텍스트뷰이다.
        //물론 여기에서 함께하기 버튼이나 함께하기 취소 버튼을 눌럿을 때 선택된 인원수가 변동이 된다.
        txt_selected_num = findViewById(R.id.txt_selected_num);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(Check_Join_Member.this));
        recyclerView.setHasFixedSize(true);


        //프로젝트 신청인원을 눌렀을 때 보내준 값을 가지고 옴.

        Intent intent = getIntent();
        project_id = intent.getIntExtra("project_id", 0);
        project_username = intent.getStringExtra("username");
        project_email = intent.getStringExtra("project_email");
        join_count = intent.getIntExtra("join_count", 0);
        selected_count = intent.getIntExtra("selected_num", 0);
        txt_member_num.setText(join_count + "");
        //신청한 인원에 대한 arraylist 생성
        listItems = new ArrayList<ListItem_Check_Join_Member>();
        uploadJoinMember();

        //프로젝트 작성자만 프로젝트 시작버튼이 보이도록 한다.
        if(project_email.equals(loadLoginEmail())){
            btn_start.setVisibility(View.VISIBLE);
        }else{
            btn_start.setVisibility(View.GONE);
        }
        //프로젝트 시작하기 버튼 누르기.
        btn_start.setOnClickListener(this);
    }

    //신청인원 업로드
    public void uploadJoinMember() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_JOIN_MEMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("project_join");
                            // JSONObject data = jsonarray.getJSONObject(0);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                ListItem_Check_Join_Member item = new ListItem_Check_Join_Member();
                                System.out.println("신청한놈들은 : " + object.getString("join_username"));
                                item.username = object.getString("join_username");
                                item.url = object.getString("join_profile");
                                item.email = object.getString("join_email");

                                if (object.getInt("selected") == 0) {
                                    item.together = false;
                                } else {
                                    item.together = true;
                                }
                                listItems.add(item);


                            }

                            RecyclerViewAdapter_Show_Join_Member adapter = new RecyclerViewAdapter_Show_Join_Member(Check_Join_Member.this, listItems);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Check_Join_Member.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Check_Join_Member.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("join_email", project_email);
                params.put("join_username", project_username);
                params.put("join_id", String.valueOf(project_id));

                return params;

            }
        };

        MySingleton.getInstance(Check_Join_Member.this).addToRequestque(stringRequest);

    }

    //프로젝트 시작 버튼을 누르면 선택된 친구들과 게시글 작성 유저의 채팅방을 만든다.
    //채팅방을 만든 유저는 곧 바로 채팅방 안으로 들어간다.
    //친구 이메일,친구 프로필이미지,친구 유저이름
    public void startProject(String history) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_CHAT_ROOM_FROM_PROJECT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            //게시글 상태를 '모집중 프로젝트'에서 '진행중인 프로젝트'상태로 변경해준다.
                            updateProjectStateDeveloping();
                            //방번호를 구하고 구해지면 채팅방 액티비티로 넘어간다.
                            getRoomNum();



                        } else {
                            Toast.makeText(Check_Join_Member.this, "2", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Check_Join_Member.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                //프로젝트 아이디, 신청한 사람의 유저네임이나 이메일 값.
                params.put("project_id", String.valueOf(project_id));
                params.put("history", getTime());
                params.put("email",loadLoginEmail() );
                params.put("selected_count", String.valueOf(selected_count));

                return params;

            }
        };

        MySingleton.getInstance(Check_Join_Member.this).addToRequestque(stringRequest);

    }
    //게시글 상태를 '모집중 프로젝트'에서 '진행중인 프로젝트'상태로 변경해준다.

    public void updateProjectStateDeveloping() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PROJECT_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            //게시글 상태를 '모집중 프로젝트'에서 '진행중인 프로젝트'상태로 변경해준다.

                            //방번호를 구하고 구해지면 채팅방 액티비티로 넘어간다.
                            //getRoomNum();



                        } else {
                            Toast.makeText(Check_Join_Member.this, "2", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Check_Join_Member.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                //프로젝트 아이디 값.
                params.put("project_id", String.valueOf(project_id));
                params.put("history", getTime());

                return params;

            }
        };

        MySingleton.getInstance(Check_Join_Member.this).addToRequestque(stringRequest);

    }

    //서버에서 방 번호 가져오기
    public void getRoomNum(){

        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, GET_CHAT_ROOM_NUM ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("room_number");

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(0);

                                int room_num = object.getInt("room_num");
                                System.out.println("방번호는 말이에요 바로 : " +room_num);
                                String username = object.getString("username");
                                String url = object.getString("profile_img");
                                //채팅 친구목록에서 열어본 친구의 프로필 액티비티에서 채팅방 액티비티로 넘어간다.
                                Intent intent = new Intent(Check_Join_Member.this, ChatRoomActivity.class);
                                intent.putExtra("room_num",room_num);
                                intent.putExtra("username",username);
                                intent.putExtra("url",url);
                                intent.putExtra("boss",loadLoginEmail());
                                intent.putExtra("project_id",project_id);
                                startActivity(intent);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Check_Join_Member.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(Check_Join_Member.this).addToRequestque(stringRequest3);

    }
    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
            String history = getTime();
                startProject(history);
        }
    }
}