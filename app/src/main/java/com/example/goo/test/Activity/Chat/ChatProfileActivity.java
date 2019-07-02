package com.example.goo.test.Activity.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Activity.AddProjectActivity;
import com.example.goo.test.Activity.MainActivity;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Chat_Room;
import com.example.goo.test.Item.Item_Chat_Room_List;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Goo on 2018-06-06.
 */

public class ChatProfileActivity extends AppCompatActivity implements View.OnClickListener {
    //1:1채팅하기를 눌렀을 때 서버에 방번호를 만들어준다.
    private static final String ADD_CHAT_ROOM_NUM = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/add_chat_room_num.php";

    private static final String GET_CHAT_ROOM_NUM = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_chat_room_num.php";
    TextView username, txt_chat, txt_stream;
    ImageView back_img, back, chat, stream;
    CircleImageView prof_pic;
    Intent intent;

    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String friend_username;
    String profile_img_url;
    String friend_email;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_friend_profile);

        //프로필 사진, 유저이름,뒤로가기버튼,채팅버튼,영상통화 버튼,뒤로가기 버튼 객체 생성
        prof_pic = findViewById(R.id.prof_pic);
        username = findViewById(R.id.username);
        back = findViewById(R.id.back);
        stream = findViewById(R.id.stream);
        chat = findViewById(R.id.chat);
        back_img = findViewById(R.id.back_img);

        //친구목록 리사이클러뷰에서 친구의 이름과 프로필 이미지 url 값을 가지고 와서 변수로 만들어 준다.
        intent = getIntent();
        friend_username = intent.getStringExtra("username");
        profile_img_url = intent.getStringExtra("url");
        friend_email = intent.getStringExtra("email");

        int room_num;
        //친구목록에서 인텐트로 가져온 프로필 사진과 유저 이름 프로필 화면에 입력해준다.
        Picasso.with(this).load(profile_img_url).error(R.mipmap.ic_launcher_round).into(prof_pic);
        username.setText(friend_username);

        //뒤로가기 버튼을 누르면 다시 친구목록으로 돌아간다.
        back.setOnClickListener(this);
        chat.setOnClickListener(this);


    }

    //1:1 채팅하기 버튼을 눌렀을 때 발생하는 행동
    public void chatOneByOne(String history){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,   ADD_CHAT_ROOM_NUM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       //1:1채팅 하기 버튼을 누르고 서버에 정상적으로 채팅방 번호 값이 생성되었다면 1 이라는 코드를 받게 된다.
                        if (response.contains("1")) {

                            getRoomNum();

                            //채팅 친구목록에서 열어본 친구의 프로필 화면 액티비티를 종료한다.
                            finish();
                        } else {
                            Toast.makeText(ChatProfileActivity.this, "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                System.out.println("친구의 이메일 값: " + friend_email);
                System.out.println("친구의 이름 값: " + friend_username);
                params.put("email", loadLoginEmail());
                params.put("friend_username", friend_username);
                params.put("friend_email", friend_email);
                params.put("history", history);
                params.put("profile", profile_img_url);

                return params;

            }
        };

        MySingleton.getInstance(ChatProfileActivity.this).addToRequestque(stringRequest);

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
                                //Item_Chat_Room_List item = new Item_Chat_Room_List();

                                int room_num = object.getInt("room_num");
                                System.out.println("방번호는 말이에요 바로 : " +room_num);
                                String username = object.getString("username");
                                String url = object.getString("profile_img");
                                //채팅 친구목록에서 열어본 친구의 프로필 액티비티에서 채팅방 액티비티로 넘어간다.
                                Intent intent = new Intent(ChatProfileActivity.this, ChatRoomActivity.class);
                                intent.putExtra("room_num",room_num);
                                intent.putExtra("username",username);
                                intent.putExtra("url",url);
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
                        Toast.makeText(ChatProfileActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(ChatProfileActivity.this).addToRequestque(stringRequest3);


    }
    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    //버튼 이벤트 모음. 1대1 채팅하기, 영상통화하기 버튼 이벤트
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:

                finish();
                break;

            case R.id.chat:
                final String history = getTime();
                chatOneByOne(history);

                break;
        }
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }
}