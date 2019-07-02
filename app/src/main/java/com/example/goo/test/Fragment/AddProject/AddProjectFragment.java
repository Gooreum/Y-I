package com.example.goo.test.Fragment.AddProject;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Activity.RegisterActivity;
import com.example.goo.test.Fragment.Home.HomeFragment;
import com.example.goo.test.Fragment.Home.NewProjectFragment_tab;
import com.example.goo.test.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-04.
 */

public class AddProjectFragment extends Fragment implements View.OnClickListener {
    private static final String PROJECT_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/add_project_content.php";
    private static final String MY_PROFILE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/show_my_profile.php";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private EditText title, content, apply_duration, dev_duration, member_num;
    private Button btn_confirm;
    private CircleImageView prof_pic;
    private TextView gps2, text_username;
    private LocationManager locationManager;
    private double longitude;
    private double latitude;

    private FusedLocationProviderClient client;
    private Button btn_gps;

    public AddProjectFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_project, container, false);

        title = rootView.findViewById(R.id.title);
        content = rootView.findViewById(R.id.content);
        btn_confirm = rootView.findViewById(R.id.btn_confirm);
        prof_pic = rootView.findViewById(R.id.prof_pic);
        text_username = rootView.findViewById(R.id.text_username);
        apply_duration = rootView.findViewById(R.id.apply_duration);
        dev_duration = rootView.findViewById(R.id.dev_duration);
        member_num = rootView.findViewById(R.id.member_num);


        //위치 접근에 대한 권한을 물어봄.
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        gps2 = rootView.findViewById(R.id.gps2);


        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return rootView;
        }
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    //  gps.setText(location.toString() + "\n" + location.getLatitude());
                    try {
                        Geocoder geocoder = new Geocoder(getContext());
                        List<Address> addresses = null;
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        String country = addresses.get(0).getCountryName();
                        String city = addresses.get(0).getLocality();
                        gps2.setText(country + ", " + city);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error" + e, Toast.LENGTH_SHORT).show();
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
        return rootView;


    }

    //모집글 작성 후 서보로 전송하기
    public void send() {
        final String Title = title.getText().toString();
        final String Content = content.getText().toString();
        final String Apply_duration = apply_duration.getText().toString();
        final String Dev_duration = dev_duration.getText().toString();
        final String Member_num = member_num.getText().toString();
        final String Gps = gps2.getText().toString();
        if (Title.equals("") || Content.equals("")) {
            Toast.makeText(getContext(), "빈칸 없이 작성해주세요.", Toast.LENGTH_SHORT).show();
        } else {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, PROJECT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                            if (response.contains("1")) {
                                Toast.makeText(getContext(), "작성 되었습니다.", Toast.LENGTH_LONG).show();

                                Bundle bundle = new Bundle();
                                NewProjectFragment_tab homeFragment = new NewProjectFragment_tab();
                                homeFragment.setArguments(bundle);

                                ((AppCompatActivity) getContext()).getSupportFragmentManager()
                                        .beginTransaction()
                                        //뒤로가기 눌렀을 때 다시 여기 화면으로 돌아오기 위해선 addtoBackStack메서드를 사용.
                                        .replace(R.id.scroll, homeFragment)
                                        .commit();
                            } else {
                                Toast.makeText(getContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }


    //모집기간 날짜 다이얼로그 띄우기
    public void popupDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext()
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
                                Picasso.with(getContext()).load(object.getString("profile")).error(R.mipmap.ic_launcher_round).into(prof_pic);
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
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", loadLoginEmail());
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                send();
                break;

            case R.id.apply_duration:
                popupDate();

                break;
        }
    }


    //위치추적 권한 물어보기 메서드.
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }
}