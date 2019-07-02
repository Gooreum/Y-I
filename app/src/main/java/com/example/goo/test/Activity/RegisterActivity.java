package com.example.goo.test.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;


import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String REGISTER_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/volleyRegister.php";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    private String username;
    private String password;
    private String email;

    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;

    public CircleImageView profile_image;
    public EditText editTextUsername;
    public EditText editTextEmail;
    public EditText editTextPassword;

    public Button buttonRegister,validateButton_email,validateButton;

    private boolean validate_email = false;
    private boolean validate_nick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profile_image = findViewById(R.id.profile_image);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        //이메일, 닉네임 중복버튼
        validateButton_email = findViewById(R.id.validateButton_email);
        validateButton = findViewById(R.id.validateButton);

        //회원가입 버튼 누르기
        buttonRegister.setOnClickListener(this);
        //프로필 이미지 눌렀을 때 갤러리 불러오기.
        profile_image.setOnClickListener(this);
        //이메일 중복버튼
        validateButton_email.setOnClickListener(this);
        //닉네임 중복버튼
        validateButton.setOnClickListener(this);

    }

    //회원가입 버튼 메서드
    private void registerUser() {
        username = editTextUsername.getText().toString();
        password = editTextPassword.getText().toString();
        email = editTextEmail.getText().toString();


        if(username.equals("") || password.equals("") || email.equals("")) {
            Toast.makeText(RegisterActivity.this, "빈칸 없이 작성해주세요.", Toast.LENGTH_LONG).show();
        }else{


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                        if (response.contains("1")) {
                            Toast.makeText(RegisterActivity.this, "회원가입을 축하합니다.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            intent.putExtra("email", email);
                            //로그인화면에 이메일 값을 보내줌.
                            setResult(2, intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME, username);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_EMAIL, email);
                params.put("profile_img",imageToString(bitmap));
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        }
    }

    //이메일 중복버튼
    public void validate_email(){
        email = editTextEmail.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                        if (response.contains("1")) {
                            Toast.makeText(RegisterActivity.this, "중복된 이메일 입니다.", Toast.LENGTH_SHORT).show();
                            validate_email = false;
                        } else {
                            Toast.makeText(RegisterActivity.this, "사용하실 수 있는 이메일 입니다. ", Toast.LENGTH_SHORT).show();
                            validate_email = true;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email_check", email);

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //닉네임 중복버튼
    public void validate_nickName(){
        username = editTextUsername.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                        if (response.contains("1")) {
                            Toast.makeText(RegisterActivity.this, "중복된 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                            validate_nick = false;
                        } else {
                            Toast.makeText(RegisterActivity.this, "사용하실 수 있는 닉네임 입니다. ", Toast.LENGTH_SHORT).show();
                            validate_nick = true;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("nickname_check", username);

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //비트맵을 jpg파일로 바꿈
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);

    }

    //갤러리 불러오기
    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }


    //회원가입 버튼 눌렀을 때의 이벤트 리스너.
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonRegister:
                if(validate_email == false || validate_nick == false){
                    Toast.makeText(this, "중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                }else if(validate_nick == true && validate_email == true){
                    registerUser();
                    System.out.println(bitmap);
                }

                break;
            case R.id.profile_image:
                selectImage();
                break;
            case R.id.validateButton_email:
                if(editTextEmail.getText().toString().isEmpty()){
                    Toast.makeText(this,"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    validate_email();
                }

                break;

            case R.id.validateButton:
                if(editTextUsername.getText().toString().isEmpty()){
                    Toast.makeText(this,"닉네임을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    validate_nickName();
                }

                break;

        }


    }

    //갤러리에서 이미지 불러오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data!=null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                profile_image.setImageBitmap(bitmap);
                profile_image.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}