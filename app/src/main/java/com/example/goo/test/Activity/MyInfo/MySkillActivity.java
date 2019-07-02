package com.example.goo.test.Activity.MyInfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Goo on 2018-05-27.
 */

public class MySkillActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String MYSKILL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/insert_myskill.php";

    Toolbar toolbar;
    private EditText myskill,explain;
    private Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_myskill);

        //자기 소개,관심사, 관심사 소개,저장버튼 생성
        myskill = findViewById(R.id.myskill);
        explain = findViewById(R.id.explain);
       // intro_myself = findViewById(R.id.intro_myself);
        btn_save = findViewById(R.id.btn_save);

        //관련 내용 저장하기.
        btn_save.setOnClickListener(this);



        //툴바관련 모음.
        toolbar =findViewById(R.id.toolBar_info);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("소개");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

    //저장하기 메서드
    //모집글 작성 후 서보로 전송하기
    public void save(){
     //   final String Intro = intro_myself.getText().toString();
        final String Myskill = myskill.getText().toString();
        final String Explain = explain.getText().toString();

        if( Myskill.equals("") || Explain.equals("")) {
            Toast.makeText(MySkillActivity.this, "작성한 내용이 없습니다.", Toast.LENGTH_SHORT).show();
        }else{


            StringRequest stringRequest = new StringRequest(Request.Method.POST, MYSKILL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                            if (response.contains("1")) {
                                Toast.makeText(MySkillActivity.this, "작성 되었습니다.", Toast.LENGTH_LONG).show();
                                finish();


                            } else {
                                Toast.makeText(MySkillActivity.this, "오류가 생겼습니다.", Toast.LENGTH_LONG).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MySkillActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", loadLoginEmail());

                    params.put("myskill", Myskill);
                    params.put("explain", Explain);

                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save :
                save();

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
