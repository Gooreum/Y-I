package com.example.goo.test.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Activity.Chat.ChatRoomActivity;
import com.example.goo.test.Fragment.AddProject.AddProjectFragment;
import com.example.goo.test.Fragment.Alarm.AlarmFragment;
import com.example.goo.test.Fragment.Home.HomeFragment;
import com.example.goo.test.Item.ListItem_friendSearch;
import com.example.goo.test.Util.BottomNavigationViewHelper;
import com.example.goo.test.Fragment.Chat.ChatFragment;
import com.example.goo.test.Fragment.MyInfo.MyInfoFragment;
import com.example.goo.test.R;
import com.example.goo.test.Fragment.Search.SearchFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Goo on 2018-04-26.
 */

public class MainActivity extends AppCompatActivity{
    private static final String UPLOAD_TOKEN = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/fcm_insert.php";
    private static final String UPDATE_CHAT_STATE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/update_chat_in_state.php";
    String email;
    //마지막으로 뒤로가기 버튼이 터치된 시간
    private long lastTimeBackPressed;

    List<ListItem_friendSearch> list;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);

        //token값 서버에 업로드 시키기.
        uploadFcmToken();


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        //프래그먼트 중에서 홈화면이 제일 첫 화면으로 나오게 한다.
       getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();


        //BottomNavigationViewHelper 클래스로부터 하단네비게이션바의 shifting 애니메이션을 없애도록 한다.
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

    }
    //token값 서버에 업로드 시키기
    public void uploadFcmToken(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("1")) {

                            System.out.println("토큰이 정상적으로 업로드 되었습니다.");
                            //Toast.makeText(MainActivity.this, "토큰이 정상적으로 업로드 되었습니다.", Toast.LENGTH_LONG).show();
                        } else if(response.contains("2")) {
                            Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }


                }) {
            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", loadLoginEmail());
                params.put("fcm_token", getFcmToken());
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    //하단 네비게이션바에서 어떤 아이콘을 선택하느냐에 따라 화면이 바뀌게 됨
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                            //홈화면
                        case R.id.nav_home:
                            //번들을 이용해서 개인화면에 로그인 할 때 가져온 이메일 값을 보내줌.
                            Bundle bundle1 = new Bundle();

                            selectedFragment = new HomeFragment();
                            selectedFragment.setArguments(bundle1);

                            break;

                        case R.id.nav_chat:
                            //번들을 이용해서 개인화면에 로그인 할 때 가져온 이메일 값을 보내줌.
                            Bundle bundle2 = new Bundle();

                            selectedFragment = new ChatFragment();
                            selectedFragment.setArguments(bundle2);
                            break;
                            //채팅 화면
                        case R.id.nav_addProject:
                            //번들을 이용해서 개인화면에 로그인 할 때 가져온 이메일 값을 보내줌.
                            Bundle bundle3 = new Bundle();

                            selectedFragment = new AlarmFragment();
                            selectedFragment.setArguments(bundle3);
                            break;
                            //사용자 검색
                        case R.id.nav_search:
                            //번들을 이용해서 개인화면에 로그인 할 때 가져온 이메일 값을 보내줌.
                            Bundle bundle4 = new Bundle();

                            //selectedFragment = new MyInfoFragment();
                            selectedFragment = new SearchFragment();
                            selectedFragment.setArguments(bundle4);
                            break;
                            //개인화면
                        case R.id.nav_myInfo:
                            //번들을 이용해서 개인화면에 로그인 할 때 가져온 이메일 값을 보내줌.
                            Bundle bundle = new Bundle();

                            selectedFragment = new MyInfoFragment();
                            selectedFragment.setArguments(bundle);
                            break;
                    }

                    //화면 전환을 가능하게 해줌.
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                   return true;
                }
            };


    //sharedpreferences에 저장된 FCM 토큰값 가져오기
    private String getFcmToken(){
        SharedPreferences fcm_token = getApplication().getSharedPreferences("fcm",MODE_PRIVATE);
        String token = fcm_token.getString("fcm","null");
        //Toast.makeText(this,"기존에 있던 토큰 값은 : " + token , Toast.LENGTH_SHORT).show();
        return token;
    }
    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getApplication().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        //System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);


        return email_from_login;
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        System.out.println("다시 시작 되었습니다.");
        makeStateOne();
        System.out.println("접속상태가 1이 되었습니다. ");
    }*/

   /* //홈버튼을 눌렀을 때 채팅 메세지를 받아 볼 수 있도록 한다.
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        makeStateZero();


    }*/

    //뒤로가기 두번 누를 때 앱 종료
  /*  @Override
    public void onBackPressed(){

        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        Toast.makeText(this,"뒤로 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }*/

    //채팅방에 접속해 있지 않을 때 접속 상태를 '0'으로 만들어 준다.
    public void makeStateZero() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_CHAT_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                        //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                        if (response.contains("1")) {
                            //내가 보낸 메세지는 서버를 거치지 않고 바로 받아 볼 수 있도록 한다.


                        } else {
                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, "Error while uploading image", Toast.LENGTH_LONG).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

               // params.put("room_num", String.valueOf(room_num));
                params.put("email_out", loadLoginEmail());


                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //채팅방에 접속해 있지 않을 때 접속 상태를 '0'으로 만들어 준다.
    public void makeStateOne() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_CHAT_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                        //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                        if (response.contains("1")) {
                            //내가 보낸 메세지는 서버를 거치지 않고 바로 받아 볼 수 있도록 한다.


                        } else {
                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, "Error while uploading image", Toast.LENGTH_LONG).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                // params.put("room_num", String.valueOf(room_num));
                params.put("email_in", loadLoginEmail());


                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
