package com.example.goo.test.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Fragment.Home.HomeFragment;
import com.example.goo.test.R;
import com.example.goo.test.Util.MyCommand;
import com.example.goo.test.Util.MySingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Goo on 2018-05-26.
 */

public class AddProjectActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String PROJECT_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/add_project_content.php";
    private static final String MY_PROFILE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/show_my_profile.php";
    private static final String PROJECT_IMAGE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/project_image.php";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    //이미지 관련 필요 변수들
    private final int GALLERY_REQUEST = 1200;
    final String TAG = this.getClass().getSimpleName();
    private Bitmap bitmap;
    private ProgressBar progressBar;

    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //마지막으로 뒤로가기 버튼이 터치된 시간
    private long lastTimeBackPressed;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private EditText title, content, apply_duration, dev_duration, member_num;
    private Button btn_confirm;
    private CircleImageView prof_pic;
    private TextView gps2, text_username,pic_num;
    private ImageView project_pic;
    private LinearLayout linearMain;
    private double longitude;
    private double latitude;
    Toolbar toolbar;

    private FusedLocationProviderClient client;

    GalleryPhoto galleryPhoto;
    ArrayList<Uri> imageList;
    ArrayList<EditText> editList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        //툴바
        toolbar = findViewById(R.id.toolBar_info);
        toolbar.setTitle("모집글 작성");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));

                finish();

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_add_project, new HomeFragment()).commit();

            }
        });

        imageList = new ArrayList<>();
        editList = new ArrayList<EditText>();
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        linearMain = findViewById(R.id.linearMain);
        pic_num = findViewById(R.id.pic_num);
        project_pic = findViewById(R.id.project_pic);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        btn_confirm = findViewById(R.id.btn_confirm);
        prof_pic = findViewById(R.id.prof_pic);
        text_username = findViewById(R.id.text_username);
        apply_duration = findViewById(R.id.apply_duration);
        dev_duration = findViewById(R.id.dev_duration);
        member_num = findViewById(R.id.member_num);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //프로젝트 이미지 눌렀을 때 갤러리 불러오기.
        project_pic.setOnClickListener(this);

        //위치 접근에 대한 권한을 물어봄.
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);

        gps2 = findViewById(R.id.gps2);
        if (ActivityCompat.checkSelfPermission(AddProjectActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(AddProjectActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    //  gps.setText(location.toString() + "\n" + location.getLatitude());
                    try {
                        Geocoder geocoder = new Geocoder(AddProjectActivity.this);
                        List<Address> addresses = null;
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        String country = addresses.get(0).getCountryName();
                        String city = addresses.get(0).getLocality();
                        gps2.setText(country + ", " + city);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(AddProjectActivity.this, "Error" + e, Toast.LENGTH_SHORT).show();
                    }
                    // gps.setText(location.getLatitude() + " ");

                } else {
                    gps2.setText("대한민국, 서울");
                }
            }
        });

        uploadProfile();
        btn_confirm.setOnClickListener(this);
        apply_duration.setOnClickListener(this);
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "년 " + month + "월 " + day + "일";
                apply_duration.setText(date);
            }
        };
    }


    //모집글 작성 후 서보로 전송하기
    public void send(String history) {
        System.out.println("사진 갯수는 : " + imageList.size());
        final int img_count = imageList.size();
        final String Title = title.getText().toString();
        final String Content = content.getText().toString();
        final String Apply_duration = apply_duration.getText().toString();
        final String Dev_duration = dev_duration.getText().toString();
        final String Member_num = member_num.getText().toString();
        final String Gps = gps2.getText().toString();
        if (Title.equals("") || Content.equals("")) {
            Toast.makeText(AddProjectActivity.this, "빈칸 없이 작성해주세요.", Toast.LENGTH_SHORT).show();
        } else {

            //final MyCommand myCommand = new MyCommand(getApplicationContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, PROJECT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                            if (response.contains("1")) {

                                // finish();
                                if (imageList.size() == 0) {
                                    Intent intent = new Intent(AddProjectActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(AddProjectActivity.this, "작성 되었습니다.", Toast.LENGTH_LONG).show();
                                }

                                // getSupportFragmentManager().beginTransaction().replace(R.id.frame_add_project, new HomeFragment()).commit();

                            } else {
                                Toast.makeText(AddProjectActivity.this, "회원가입 실패", Toast.LENGTH_LONG).show();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddProjectActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("content", Content);
                    params.put("title", Title);
                    params.put("email", loadLoginEmail());
                    params.put("apply_duration", Apply_duration);
                    params.put("dev_duration", Dev_duration);
                    params.put("member_num", Member_num);
                    params.put("gps", Gps);
                    params.put("history", history);
                    params.put("img_count", String.valueOf(img_count));
                    System.out.println("프로젝트의 시간 : " + history);
                    return params;

                }
            };

            MySingleton.getInstance(AddProjectActivity.this).addToRequestque(stringRequest);

        }
    }


    //서버에 이미지 전송
    public void sned_image(String history) {
        progressBar.setVisibility(View.VISIBLE);
        final MyCommand myCommand = new MyCommand(getApplicationContext());
        System.out.println("사진 갯수는 : " + imageList.size());
        final int img_count = imageList.size();
        final String Title = title.getText().toString();
        final String Content = content.getText().toString();
        final String Apply_duration = apply_duration.getText().toString();
        final String Dev_duration = dev_duration.getText().toString();
        final String Member_num = member_num.getText().toString();
        final String Gps = gps2.getText().toString();
        if (Title.equals("") || Content.equals("")) {
            Toast.makeText(AddProjectActivity.this, "빈칸 없이 작성해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            //이미지 따로 보내기
            for (Uri imagePath : imageList) {
                    try {
                    //  Bitmap bitmap = PhotoLoader.init().from(imagePath).requestSize(512, 512).getBitmap();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);

                    // String encodedString = ImageBase64.encode(bitmap);
                    String encodedString = imageToString(bitmap);

                    StringRequest stringRequest_image = new StringRequest(Request.Method.POST, PROJECT_IMAGE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                    // Toast.makeText(AddProjectActivity.this, response, Toast.LENGTH_SHORT).show();

                                    //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                                    //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                                    if (imageList.size() == 1 && response.contains("1")) {
                                        Intent intent = new Intent(AddProjectActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(AddProjectActivity.this, "작성 되었습니다.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        finish();
                                    }
                                    if (imageList.size() == 2 && response.contains("2")) {

                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(AddProjectActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(AddProjectActivity.this, "작성 되었습니다.", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    if (imageList.size() == 3 && response.contains("3")) {
                                        progressBar.setVisibility(View.GONE);

                                        Intent intent = new Intent(AddProjectActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(AddProjectActivity.this, "작성 되었습니다.", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    if (imageList.size() == 4 && response.contains("4")) {
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(AddProjectActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(AddProjectActivity.this, "작성 되었습니다.", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    if (imageList.size() == 5 && response.contains("5")) {
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(AddProjectActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(AddProjectActivity.this, "작성 되었습니다.", Toast.LENGTH_LONG).show();
                                        finish();

                                    }


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(AddProjectActivity.this, "Error while uploading image", Toast.LENGTH_LONG).show();
                                }


                            }) {
                        @Override
                        protected Map<String, String> getParams() {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("image", encodedString);
                            params.put("history", history);
                            params.put("list_num", String.valueOf(imageList.indexOf(imagePath)));
                            System.out.println("에디트 텍스트에서 한 말은 : " + editList.get(imageList.indexOf(imagePath)).getText().toString());
                            params.put("edit", editList.get(imageList.indexOf(imagePath)).getText().toString());
                            System.out.println("이미지 리스트의 사이즈는 : " + imageList.size());

                            return params;

                        }
                    };

                    myCommand.add(stringRequest_image);


                } catch (FileNotFoundException e) {
                    Toast.makeText(AddProjectActivity.this, "Error while loading image", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


            myCommand.execute();
        }
    }

    //모집기간 날짜 다이얼로그 띄우기
    public void popupDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(AddProjectActivity.this
                , android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    //프로필 사진, 닉네임, 이메일 값 불러오기.
    public void uploadProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MY_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("profile");

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                //프로필 이미지
                                Picasso.with(AddProjectActivity.this).load(object.getString("profile")).error(R.mipmap.ic_launcher_round).into(prof_pic);
                                text_username.setText(object.getString("username").toString());


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddProjectActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", loadLoginEmail());
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddProjectActivity.this);
        requestQueue.add(stringRequest);
    }


    //위치추적 권한 물어보기 메서드.
    private void requestPermission() {
        ActivityCompat.requestPermissions(AddProjectActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


    //비트맵을 jpg파일로 바꿈
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    //갤러리 불러오기
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent,GALLERY_REQUEST);
        startActivityForResult(Intent.createChooser(intent, "다중 선택은 '포토'를 선택하세요."), GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {

            int count = data.getClipData().getItemCount();

            pic_num.setText("선택한 사진 갯수 : " + count);
            for (int i = 0; i < count; i++) {
                Uri path = data.getClipData().getItemAt(i).getUri();
                //갤러리에서 불러온 여러 이미지의 경로를 가지고옴. getClipData().getItemAt(i).getUri() 메서드를 반드시 써줘야함.
                //그냥 getPath()는 이미지 한개만 불러옴.
                galleryPhoto.setPhotoUri(data.getClipData().getItemAt(i).getUri());

                String photoPath = galleryPhoto.getPath();

                imageList.add(path);

                // Log.d(TAG, galleryPhoto.getPath());
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

                    ImageView imageView = new ImageView(getApplicationContext());

                    EditText editText = new EditText(getApplicationContext());

                    editText.setHint("내용");
                    editText.setHintTextColor(Color.GRAY);
                    editText.setTextColor(Color.BLACK);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.bottomMargin = 150;
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(10, 10, 10, 10);

                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    //앨범에서 고른 사진의 갯수만큼 라이니어레이아웃에서 이미지뷰를 추가한다.

                    editText.setLayoutParams(layoutParams);

                    linearMain.addView(imageView);
                    linearMain.addView(editText);

                    editList.add(editText);

                    //이미지를 클릭하면 해당 이미지는 삭제됨.
                    /*심각한 문제가 발생..
                    * 갤러리에서 불러온 이미지를 가져오고 나서 삭제를 하게 되면 index문제가 발생.
                    * 5개의 이미지가 있다면, [0]번째 이미지를 삭제 후 인덱스의 변화가 생기지 않음.지워진 이미지의 인덱스는 그대로 남아있게 됨.
                    * */

                    int finalI = i;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //null값을 줘야 화면에서도 이미지가 사라짐.
                            imageView.setImageBitmap(null);
                            //remove를 해주어야 이미지를 담고 있는 imageList에서도 이미지가 삭제되어 서버에 전송이 되지 않는다.
                            System.out.println("사진의 i 값은 : " + finalI);

                            imageList.remove(finalI);
                            imageView.setVisibility(View.GONE);
                            editList.remove(finalI);
                            editText.setVisibility(View.GONE);

                        }
                    });

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Error while loading image", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                final String history = getTime();
                send(history);
                sned_image(history);
//                System.out.println("내가 한 말은 " + editList.get(0).getText().toString());
                break;

            case R.id.apply_duration:
                popupDate();

                break;

            case R.id.project_pic:
                selectImage();
                break;

        }
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


    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }
}