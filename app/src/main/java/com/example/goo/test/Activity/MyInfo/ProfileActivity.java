package com.example.goo.test.Activity.MyInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Dialog.Dialog_Profile_Image_Camera_Or_Gallery;
import com.example.goo.test.Dialog.Dialog_Project_Url;
import com.example.goo.test.Fragment.MyInfo.MyInfoFragment;
import com.example.goo.test.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PROFILE_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/profile.php";
    private static final String PROFILE_MOTIFY_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/profile_motify.php";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    private String username;
    private String password;
    private String email;

    //이미지 관련 필요 변수들
    private final int IMG_REQUEST = 1;
    private final int OPENCV_REQUEST = 2;
    private Bitmap bitmap;

    public CircleImageView profile_image;
    public EditText editTextUsername;
    public TextView editTextEmail;
    public EditText editTextPassword;

    //프로필 이미지 변경 버튼.
    private Button gallery, camera;

    public Button buttonRegister;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_image = findViewById(R.id.profile_image);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);


        //프로필 이미지 변경 버튼
        gallery = findViewById(R.id.gallery);
        camera = findViewById(R.id.camera);
        //갤러리 버튼 클릭시 갤러리 화면으로.
        gallery.setOnClickListener(this);

        //카메라 버튼 클릭시 오픈씨브이 화면으로
        camera.setOnClickListener(this);

        //회원가입 버튼 누르기
        buttonRegister.setOnClickListener(this);
        //프로필 이미지 눌렀을 때 갤러리 불러오기.
        profile_image.setOnClickListener(this);

        toolbar = findViewById(R.id.toolBar_info);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                //프래그먼트 중에서 홈화면이 제일 첫 화면으로 나오게 한다.
                finish();

                getSupportFragmentManager().beginTransaction().replace(R.id.profile_frame, new MyInfoFragment()).commit();

            }
        });


        //오픈씨브이를 통해 프로필 이미지를 변경할 경우.
        //opencv에서 캡쳐한 이미지를 인텐트로 받아낸다.
        //이미지 경로를 파일 이름으로 받아 냈고,
        // 파일을 이미지뷰에 그려주기 위해서는 아래와 같은 방식으로 코드를 작성해주어야 한다.
        upload();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //회원가입 버튼 메서드
    private void upload() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("member");
                            JSONObject data = jsonArray.getJSONObject(0);
                            editTextUsername.setText(data.getString("username"));
                            editTextPassword.setText(data.getString("password"));
                            editTextEmail.setText(data.getString("email"));
                            String url = data.getString("profile");
                            Picasso.with(ProfileActivity.this).invalidate(url);
                            Picasso.with(ProfileActivity.this)
                                    .load(url)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(profile_image);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                //params.put(KEY_USERNAME, username);
                //params.put(KEY_PASSWORD, password);
                params.put("email", loadLoginEmail());
                //  params.put("profile_img",imageToString(bitmap));
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void confirm() {
        username = editTextUsername.getText().toString();
        password = editTextPassword.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_MOTIFY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                        if (response.contains("1")) {
                            Toast.makeText(ProfileActivity.this, "프로필이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent(ChatProfileActivity.this, MyInfoFragment.class);
                            // startActivity(intent);
                           //getSupportFragmentManager().beginTransaction().replace(R.id.profile_frame, new MyInfoFragment()).commit();

                            finish();
                        } else if (response.contains("2")) {
                            Toast.makeText(ProfileActivity.this, "변경사항이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                //사진이 없는 경우 보내는 값들
                if (bitmap == null) {
                    params.put(KEY_USERNAME, username);
                    params.put(KEY_PASSWORD, password);
                    params.put("email", loadLoginEmail());
                    System.out.println("bbbbbbbbbbbbbb" + loadLoginEmail());
                }
                //사진이 있는 경우 보내는 값들
                else {
                    params.put(KEY_USERNAME, username);
                    params.put(KEY_PASSWORD, password);
                    //params.put("email",loadLoginEmail());
                    params.put("profile_img", imageToString(bitmap));
                    params.put("email_bitmap", loadLoginEmail());
                    System.out.println("wwwwwwwwwwwwwwww" + bitmap.toString());
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //비트맵을 jpg파일로 바꿈
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    //갤러리 불러오기
    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    //회원가입 버튼 눌렀을 때의 이벤트 리스너.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:

                confirm();
                //System.out.println(bitmap);
                break;
            case R.id.profile_image:
                // selectImage();
                // openDialog();
                break;

            //오픈씨브이 화면으로 넘겨준다.
            case R.id.camera:
                Intent intent = new Intent(ProfileActivity.this, OpenCV.class);
                startActivityForResult(intent, OPENCV_REQUEST);
                break;

            case R.id.gallery:
                selectImage();

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                profile_image.setImageBitmap(bitmap);
                System.out.println("프로필 이미지 비트맵 값은 " + bitmap.toString());
                System.out.println("프로필 이미지 비트맵 값은 " + path);
                profile_image.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == OPENCV_REQUEST && resultCode == 2 && data != null) {
            File pictureFile = (File)data.getExtras().get("file");
            String path = data.getStringExtra("uri");
            System.out.println("ㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂ" + path);

            bitmap = BitmapFactory.decodeFile(path);
            profile_image.setImageBitmap(bitmap);


            //오픈씨브이로 찍은 이미지를 갤러리에 저장
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(pictureFile));
            sendBroadcast(mediaScanIntent);

        }


    }

    //쉐어드로 로그인 아이디 가져오기.
    private String loadLoginEmail() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        // System.out.println(email_from_login);


        return email_from_login;
    }

}