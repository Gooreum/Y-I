package com.example.goo.test.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.bumptech.glide.Glide;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
import com.example.goo.test.Activity.Home.Check_Join_Member;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Join_Member;
import com.example.goo.test.Item.ListItem_Check_Join_Member;
import com.example.goo.test.R;
import com.example.goo.test.Util.Common;
import com.example.goo.test.Util.MyFireBaseInstanceIDService;
import com.example.goo.test.Util.MySingleton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOGIN_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/volleyLogin.php";

    public LoginActivity() {
    }


    //getParams 메서드에 넣어줄 키 선언
    private static final String HOME_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/Login_Google_API.php";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    private String username;
    private String password;
    private String email;


    //마지막으로 뒤로가기 버튼이 터치된 시간
    private long lastTimeBackPressed;


    private FusedLocationProviderClient client;
    private TextView gps;


    private LinearLayout Prof_Section;
    private Button SignOut;
    private SignInButton SignIn;
    private TextView Name, Email;
    private ImageView Prof_pic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    public EditText editTextEmail;
    public EditText editTextPassword;
    public TextView registerButton;
    private CheckBox rememberId;
    private CheckBox autoLogin;
    public Button LoginButton;
    // StringRequest stringRequest;

    //페이스북 연동 코드
//    LoginButton loginButton;
//    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        System.out.println("시간 : " + System.currentTimeMillis());
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        LoginButton = findViewById(R.id.LoginButton);
        registerButton = findViewById(R.id.registerButton);
        rememberId = findViewById(R.id.rememberId);
        autoLogin = findViewById(R.id.autoLogin);


        //구글 연동 관련 xml코드
        Prof_Section = findViewById(R.id.prof_section);
        SignOut = findViewById(R.id.bn_logout);
        SignIn = findViewById(R.id.bn_login);
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Prof_pic = findViewById(R.id.prof_pic);
        SignIn.setOnClickListener(this);
        SignOut.setOnClickListener(this);


        //아이디 기억하기 체크박스가 눌러 졌을 경우 이메일 입력창에 마지막으로 로그인한 이메일 값 입력하기.
        SharedPreferences sp = getSharedPreferences("ID", MODE_PRIVATE);
        String email_from_login = sp.getString("id", null);
        rememberId.setChecked(sp.getBoolean("checkState", rememberId.isChecked()));
        // editTextEmail.setText(email_from_login);
        if (rememberId.isChecked() == true) {
            editTextEmail.setText(email_from_login);
        }

        //로그인버튼 눌렀을 경우
        LoginButton.setOnClickListener(this);
        //회원가입 버튼 눌렀을 경우
        registerButton.setOnClickListener(this);
        //아이디 기억하기 체크박스 눌렀을 경우
        rememberId.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rememberId.isChecked() == true) {
                    System.out.println("눌러졌다 이놈아");
                    SharedPreferences ID = getApplicationContext().getSharedPreferences("ID", MODE_PRIVATE);
                    SharedPreferences.Editor editor = ID.edit();
                    editor.putString("id", editTextEmail.getText().toString());
                    editor.putBoolean("checkState", rememberId.isChecked());
                    editor.commit();

                } else {
                    //로그아웃 할 경우에 쉐어드에 저장해둔 이메일을 삭제하고, 로그인 화면으로 이동한다.
                    SharedPreferences pref = getSharedPreferences("ID", MODE_PRIVATE); // 선언
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();

                }
            }
        });

        Prof_Section.setVisibility(View.GONE);


        //페이스북 연동 코드들
//        FacebookSdk.sdkInitialize(getApplicationContext());
//
//
//        loginButton = (LoginButton)findViewById(R.id.fb_login_bn);
//        callbackManager = CallbackManager.Factory.create();
//        loginButton.setReadPermissions(Arrays.asList("public_profile","email","name"));
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                //페이스북 토큰 값 가지고 오기
//                Prof_Section.setVisibility(View.VISIBLE);
//                Email.setText("Login Success \n" + loginResult.getAccessToken().getUserId() + loginResult.getAccessToken().getToken() );
//
//            }
//
//            @Override
//            public void onCancel() {
//                Email.setText("Login Cancelled");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//

        //구글로부터 값 가져오기
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //회원가입 이후 이메일 값 인텐트로 가져오기
        if (requestCode == 1 && resultCode == 2) {

            String email = data.getStringExtra("email");
            editTextEmail.setText(email);

        }

        //구글에 대한 요청 코드
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);


            username = Name.getText().toString();
            email = Email.getText().toString();
            password = "000";
            //요청이 완료되면 홈 화면으로 넘어가도록 한다.
            googleSigIn_after();

        }
        //페이스북 결과값
        // callbackManager.onActivityResult(requestCode,resultCode,data);
    }


    //로그인 메서드(페이스북, 구글이 아님, 앱 자체 로그인)
    public void login() {

        password = editTextPassword.getText().toString();
        email = editTextEmail.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("1")) {
                            System.out.println("통신 성공");

                            //쉐어드를 이용해서 로그인 할 경우 이메일 값을 저장함.
                            SharedPreferences login = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = login.edit();
                            editor.putString("Login", editTextEmail.getText().toString());
                            editor.commit();

                            //fcm 토큰 값 가져오기
                            String token = FirebaseInstanceId.getInstance().getToken();
                            //만약 토큰 값이 없으면 새로운 토큰 값을 가져 온다.
                            System.out.println("토큰값은 : " + getFcmToken());
                            System.out.println("방금 가져온 토큰값은 : " + token);
                            if (getFcmToken().equals("null") || !getFcmToken().equals(token)) {
                                //토큰 값을 shared로 저장한다.


                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("fcm", MODE_PRIVATE);
                                SharedPreferences.Editor editor_token = sharedPreferences.edit();
                                editor_token.putString("fcm", token);
                                editor_token.commit();
                                //Toast.makeText(LoginActivity.this,"새로 저장한 토큰 값은 : "+ token , Toast.LENGTH_SHORT).show();

                                //만약 fcm 토큰 값과 shared에 저장된 토큰 값이 다르다면 새로 갱신된 토큰 값을 저장해준다.
                            }
                            //로그인 성공후 유저네임과 프로필 이미지갑 가지고 오기.
                            getProfile(editTextEmail.getText().toString());
                          /*  else {
                                if(!getFcmToken().equals(token)){
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("fcm",MODE_PRIVATE);
                                    SharedPreferences.Editor editor_token = sharedPreferences.edit();
                                    editor_token.putString("fcm",token);
                                    editor_token.commit();
                                }
                            }*/
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("email", editTextEmail.getText().toString());
                            startActivity(intent);


                        } else if (response.contains("2")) {
                            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 다시 입력하세요.", Toast.LENGTH_LONG).show();
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

                params.put("email", email);
                params.put("password", password);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //페이스북 로그인 api  연동 코드들


    //구글 연동관련 코드들
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //구글 로그인 버튼
    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);

    }

    //구글 로그아웃 버튼
    public void signOut() {

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    //연동 결과
    private void handleResult(GoogleSignInResult result) {

        //로그인이 성공한다면 이름과 이메일을 가져 온다.
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name_google = account.getDisplayName();
            String email_google = account.getEmail();

            String img_url = account.getPhotoUrl().toString();
            Name.setText(name_google);
            Email.setText(email_google);
            Name.setVisibility(View.GONE);
            Email.setVisibility(View.GONE);
            Picasso.with(this).load(img_url).into(Prof_pic);
            Prof_pic.setVisibility(View.GONE);
            updateUI(true);
        }
        //로그인 실패시
        else {
            updateUI(false);
        }
    }


    private void updateUI(boolean isLogin) {
        if (isLogin) {
            // Prof_Section.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
        } else {
            Prof_Section.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);
        }
    }


    //구글 연동 후 구글 로그인 한 경험이 있을 경우 select문, 처음 로그인 경우 db에 저장하기
    private void googleSigIn_after() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                        if (response.contains("1")) {
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.putExtra("email", Email.getText().toString());
//
//                            startActivity(intent);
//                            Toast.makeText(LoginActivity.this, "환영합니다.", Toast.LENGTH_LONG).show();
                            //쉐어드를 이용해서 로그인 할 경우 이메일 값을 저장함.
                            SharedPreferences login = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = login.edit();
                            editor.putString("Login", Email.getText().toString());
                            editor.commit();

                            //fcm 토큰 값 가져오기
                            String token = FirebaseInstanceId.getInstance().getToken();
                            //만약 토큰 값이 없으면 새로운 토큰 값을 가져 온다.
                            System.out.println("토큰값은 : " + getFcmToken());
                            System.out.println("방금 가져온 토큰값은 : " + token);
                            if (getFcmToken().equals("null") || !getFcmToken().equals(token)) {
                                //토큰 값을 shared로 저장한다.

                                //String token = FirebaseInstanceId.getInstance().getToken();
                                //Log.d("My Token ", Common.currentToken);

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("fcm", MODE_PRIVATE);
                                SharedPreferences.Editor editor_token = sharedPreferences.edit();
                                editor_token.putString("fcm", token);
                                editor_token.commit();
                                // Toast.makeText(LoginActivity.this,"새로 저장한 토큰 값은 : "+ token , Toast.LENGTH_SHORT).show();


                                //만약 fcm 토큰 값과 shared에 저장된 토큰 값이 다르다면 새로 갱신된 토큰 값을 저장해준다.
                            }

                            //로그인 성공후 유저네임과 프로필 이미지갑 가지고 오기.
                            getProfile(Email.getText().toString());

                           /* else {
                                if(!getFcmToken().equals(token)){
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("fcm",MODE_PRIVATE);
                                    SharedPreferences.Editor editor_token = sharedPreferences.edit();
                                    editor_token.putString("fcm",token);
                                    editor_token.commit();
                                }
                            }*/


                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("email", Email.getText().toString());
                            startActivity(intent);

                            //startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME, username);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_EMAIL, email);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //뒤로가기 두번 누를 때 앱 종료
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    //버튼 클릭 리스너
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bn_login:
                signIn();
                break;

            case R.id.bn_logout:
                signOut();
                break;

            case R.id.LoginButton:
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    login();

                }
                break;

            case R.id.registerButton:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    //유저의 닉네임과 프로필 이미지값 가지고 오기
    private void getProfile(String email5) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("profile_info");
                            // JSONObject data = jsonarray.getJSONObject(0);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);

                                String username = object.getString("username");
                                String profile_url = object.getString("profile");


                                System.out.println("ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ" + username);
                                System.out.println("ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ" + profile_url);
                                SharedPreferences ID = getApplicationContext().getSharedPreferences("profile_info", MODE_PRIVATE);
                                SharedPreferences.Editor editor = ID.edit();
                                editor.putString("username", username);
                                editor.putString("profile_url", profile_url);
                                editor.commit();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email_for_profile_info", email5);


                return params;

            }
        };

        MySingleton.getInstance(LoginActivity.this).addToRequestque(stringRequest2);
    }

    //쉐어드로 로그인 아이디 가져오기.
    private String loadLoginEmail() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        // System.out.println(email_from_login);


        return email_from_login;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    //sharedpreferences에 저장된 FCM 토큰값 가져오기
    private String getFcmToken() {
        SharedPreferences fcm_token = getApplication().getSharedPreferences("fcm", MODE_PRIVATE);
        String token = fcm_token.getString("fcm", "null");
        //Toast.makeText(this,"기존에 있던 토큰 값은 : " + token , Toast.LENGTH_SHORT).show();
        return token;
    }
}

