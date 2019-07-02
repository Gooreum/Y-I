package com.example.goo.test.Fragment.MyInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Activity.MyInfo.Recommend_activity;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Career;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Interesting;
import com.example.goo.test.Adapter.RecyclerViewAdapter_MySkill;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Recommend;
import com.example.goo.test.Item.Item_Career;
import com.example.goo.test.Item.Item_Interesting;
import com.example.goo.test.Item.Item_MySkill;
import com.example.goo.test.Item.Item_Recommend;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-27.
 */

public class MyInfo_tab_profile_By_Friend extends Fragment implements View.OnClickListener {
    private static final String HOME_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/Home.php";
    private static final String FOLLOWING_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/followCount.php";
    private static final String FOLLOWER_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/followerCount.php";
    private static final String INTEREST_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_interest.php";
    private static final String POST_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/postCount.php";
    private static final String CAREER_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_career.php";
    private static final String MYSKILL_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_myskill.php";
    private static final String RECOMMEND_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_recommend.php";
    public static final String KEY_EMAIL = "email";
    String email2;
    TextView email, name, txt_content_count, txt_follower_count, txt_following_count, intro, bottom_interest, bottom_myskill, bottom_career, bottom_exp,bottom_recommend;
    ImageView btn_profile_detail,add_interest,add_career,add_myskill,add_exp,add_recommend;
    LinearLayout post, line_follower, line_following;
    CircleImageView prof_img;
    private FloatingActionButton fab;
    List<Item_Interesting> listItems;
    List<Item_Career> list_career;
    List<Item_MySkill> list_myskill;
    List<Item_Recommend> list_recommend;

    RecyclerView recyclerView_interest;
    RecyclerView recyclerView_career;
    RecyclerView recyclerView_myskill;
    RecyclerView recyclerView_recommend;
    NestedScrollView scrollView;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_my_profile, container, false);

        email = rootView.findViewById(R.id.email);
        name = rootView.findViewById(R.id.name);
        txt_content_count = rootView.findViewById(R.id.txt_content_count);
        txt_follower_count = rootView.findViewById(R.id.txt_follower_count);
        txt_following_count = rootView.findViewById(R.id.txt_following_count);
        //intro = rootView.findViewById(R.id.intro);
        line_follower = (LinearLayout) rootView.findViewById(R.id.follower);
        line_following = (LinearLayout) rootView.findViewById(R.id.following);
        prof_img = (CircleImageView) rootView.findViewById(R.id.prof_pic);
        btn_profile_detail = rootView.findViewById(R.id.btn_profile_detail);
        bottom_interest = rootView.findViewById(R.id.bottom_interest);
        bottom_myskill = rootView.findViewById(R.id.bottom_myskill);
        bottom_career = rootView.findViewById(R.id.bottom_career);
        bottom_exp = rootView.findViewById(R.id.bottom_exp);
        bottom_recommend = rootView.findViewById(R.id.bottom_recommend);


        //개인 프로필 화면에서 이력,관심사,보유기술,등의 프로필 내역을 입력할 수 있는 버튼.
        add_career = rootView.findViewById(R.id.add_career);
        add_exp = rootView.findViewById(R.id.add_exp);
        add_interest = rootView.findViewById(R.id.add_interest);
        add_myskill = rootView.findViewById(R.id.add_myskill);
        add_recommend = rootView.findViewById(R.id.add_recommend);
        //친구의 이메일 값과 나의 이메일 값이 다르면 프로필 입력 버튼을 숨긴다.
        if(!loadLoginEmail().equals(loadLoginEmail_mine())){
            add_career.setVisibility(View.GONE);
            add_exp.setVisibility(View.GONE);
            add_interest.setVisibility(View.GONE);
            add_myskill.setVisibility(View.GONE);
            btn_profile_detail.setVisibility(View.GONE);
        }


        recyclerView_interest = rootView.findViewById(R.id.recycler_view_interest);
        recyclerView_interest.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_interest.setHasFixedSize(true);

        recyclerView_career = rootView.findViewById(R.id.recycler_view_career);
        recyclerView_career.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_career.setHasFixedSize(true);

        recyclerView_myskill = rootView.findViewById(R.id.recycler_view_myskill);
        recyclerView_myskill.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_myskill.setHasFixedSize(true);

        recyclerView_recommend = rootView.findViewById(R.id.recycler_view_recommend);
        recyclerView_recommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_recommend.setHasFixedSize(true);


        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);




        //각각의 리사이클러뷰에 해당하는 '더보기'버튼을 눌렀을 때 상세한 리사이클러뷰 화면으로 전환
        bottom_interest.setOnClickListener(this);
        bottom_myskill.setOnClickListener(this);
        bottom_career.setOnClickListener(this);
        bottom_exp.setOnClickListener(this);

        //추천서 추가 버튼

        add_recommend.setOnClickListener(this);

        //각각의 아이템 arraylist 객체 생성
        listItems = new ArrayList<>();
        list_career = new ArrayList<>();
        list_myskill = new ArrayList<>();
        list_recommend = new ArrayList<>();


        //서버로부터 프로필 값을 가져온다.
        update();

        //팔로잉 수 가져오기
        update_following_count();
        //팔로워 수 가져오기
        update_follower_count();
        //포스트 수 가져오기
        getPostCount();
        //관심분야 가져오기
        getInterest();
        //경력 가져오기
        getCareer();
        //보유기술 가져오기
        getMySkill();
        //추천서 가져오기
        getRecommend();

        //팔로잉 수 혹은 팔로워 수를 눌렀을 때 그 사람들의 목록을 띄운다.
        line_follower.setOnClickListener(this);
        line_following.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        Log.d(this.getClass().getSimpleName(), "onResume()");
        super.onResume();
        update();
        //팔로잉 수 가져오기
        update_following_count();
        //팔로워 수 가져오기
        update_follower_count();
        //포스트 수 가져오기
        getPostCount();
        //관심분야 가져오기
        //  getInterest();

    }

    //서버로부터 프로필 값을 가져오는 메서드
    private void update() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("member");
                            JSONObject data = jsonarray.getJSONObject(0);

                            String username = data.getString("username");
                            String email5 = data.getString("email");
                            String url = data.getString("profile");

                            System.out.println("url은 " + url);

                            Picasso.with(getContext()).invalidate(url);

                            Picasso.with(getContext()).load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.mipmap.ic_launcher_round).into(prof_img);

                         /*   Picasso.with(getContext())
                                    .load(url)
                                    .into(prof_img);

*/
                            name.setText(username);
                            email.setText(email5);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

        // RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Adding request to the queue
        // requestQueue.add(stringRequest);

    }


    //서버로부터 팔로우 수 가져오기
    private void update_following_count() {

        //메인액티비티에서 이메일 값을 보내는 경우 받아온다.
        if (getArguments() != null) {
            email2 = this.getArguments().getString("email").toString();

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FOLLOWING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("follow");

                            //JSONObject data = jsonarray.getJSONObject(1);

                            int following = jsonarray.length();
                            //String email5 = data.getString("email");

                            txt_following_count.setText(following + "");
                            // email.setText(email5);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }

    //서버로부터 팔로워 수 가져오기
    private void update_follower_count() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, FOLLOWER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("follow");

                            int follower = jsonarray.length();


                            txt_follower_count.setText(follower + "");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }

    //서버로부터 팔로워 수 가져오기
    private void getPostCount() {

        //메인액티비티에서 이메일 값을 받아온다.
        if (getArguments() != null) {
            email2 = this.getArguments().getString("email").toString();

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("project_content");

                            //JSONObject data = jsonarray.getJSONObject(0);

                            int post = jsonarray.length();


                            txt_content_count.setText(post + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }

    //관심사 가져오기
    public void getInterest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INTEREST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("interest");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_Interesting item = new Item_Interesting();
                                item.reason = object.getString("reason");
                                item.section = object.getString("section");

                                System.out.println(item.reason);
                                System.out.println(item.section);
                                listItems.add(item);


                            }
                            //데이터가 하나도 없으면 '더보기' 버튼을 안보이게 처리.
                            if(jsonarray.length() == 0){
                                bottom_interest.setVisibility(View.GONE);

                            }else{
                                bottom_interest.setVisibility(View.VISIBLE);
                            }

                            RecyclerViewAdapter_Interesting adapter = new RecyclerViewAdapter_Interesting(getContext(), listItems);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

                            recyclerView_interest.setLayoutManager(layoutManager);

                            recyclerView_interest.setAdapter(adapter);
                            recyclerView_interest.setNestedScrollingEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }

    //경력 가져오기
    public void getCareer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CAREER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("career");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_Career item = new Item_Career();
                                item.username = object.getString("username");
                                item.email = object.getString("email");
                                item.position = object.getString("position");
                                item.company = object.getString("company");
                                item.start_day = object.getString("start_day");
                                item.end_day = object.getString("end_day");
                                item.explain = object.getString("explain");

                                System.out.println(item.username);
                                System.out.println(item.email);
                                System.out.println(item.position);
                                System.out.println(item.company);
                                System.out.println(item.start_day);
                                System.out.println(item.end_day);
                                System.out.println(item.explain);

                                list_career.add(item);



                            }
                            //데이터가 하나도 없으면 '더보기' 버튼을 안보이게 처리.
                            if(jsonarray.length() == 0){
                                bottom_career.setVisibility(View.GONE);
                            }else{
                                bottom_career.setVisibility(View.VISIBLE);
                            }

                            RecyclerViewAdapter_Career adapter = new RecyclerViewAdapter_Career(getContext(), list_career);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

                            recyclerView_career.setLayoutManager(layoutManager);

                            recyclerView_career.setAdapter(adapter);
                            recyclerView_career.setNestedScrollingEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }


    //경력 가져오기
    public void getMySkill() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, MYSKILL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("MySkill");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_MySkill item = new Item_MySkill();
                                item.username = object.getString("username");
                                item.email = object.getString("email");
                                item.myskill = object.getString("myskill");
                                item.explain = object.getString("explain");


                                list_myskill.add(item);



                            }
                            //데이터가 하나도 없으면 '더보기' 버튼을 안보이게 처리.
                            if(jsonarray.length() == 0){
                                bottom_myskill.setVisibility(View.GONE);
                            }else{
                                bottom_myskill.setVisibility(View.VISIBLE);
                            }

                            RecyclerViewAdapter_MySkill adapter = new RecyclerViewAdapter_MySkill(getContext(), list_myskill);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

                            recyclerView_myskill.setLayoutManager(layoutManager);

                            recyclerView_myskill.setAdapter(adapter);
                            //nestedscrollview가 매끄럽게 움직이게 해줌.
                            recyclerView_myskill.setNestedScrollingEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }


    //추천서 가져오기
    public void getRecommend() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECOMMEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("recommend");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_Recommend item = new Item_Recommend();
                                item.username = object.getString("username");
                                item.email = object.getString("email");
                                item.url = object.getString("profile");
                                item.recommend_content = object.getString("recommend_content");
                                item.history = object.getString("history");


                                list_recommend.add(item);



                            }
                            //데이터가 하나도 없으면 '더보기' 버튼을 안보이게 처리.
                            if(jsonarray.length() == 0){
                                bottom_recommend.setVisibility(View.GONE);
                            }else{
                                bottom_recommend.setVisibility(View.VISIBLE);
                            }

                            RecyclerViewAdapter_Recommend adapter = new RecyclerViewAdapter_Recommend(getContext(), list_recommend);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

                            recyclerView_recommend.setLayoutManager(layoutManager);

                            recyclerView_recommend.setAdapter(adapter);
                            //nestedscrollview가 매끄럽게 움직이게 해줌.
                            recyclerView_recommend.setNestedScrollingEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }


    //버튼 이벤트 모음.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //팔로잉 중인 친구들을 볼 수 있다.
            case R.id.following:


                Following_Fragment following_fragment = new Following_Fragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.myinfo, following_fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;

                //팔로워들을 볼 수 있다.
            case R.id.follower:

                Follower_Fragment follower_fragment = new Follower_Fragment();
                FragmentTransaction fragmentTransaction2 = getFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.myinfo, follower_fragment);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                break;

                //해당 친구에 대한 추천서를 쓸 수 있다.
            case R.id.add_recommend :
                Intent intent = new Intent(getContext(), Recommend_activity.class);
                startActivity(intent);
                break;


            case R.id.bottom_interest :

                break;


            case R.id.bottom_career :

                break;


            case R.id.bottom_myskill :

                break;

            case R.id.bottom_exp :

                break;
        }
    }

    //친구 이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("friend_email", MODE_PRIVATE);
        String email_from_login = sp.getString("friend_email", null);
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);

        return email_from_login;
    }

    //내 이메일 값 가져오기
    private String loadLoginEmail_mine() {
        SharedPreferences sp = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);

        return email_from_login;
    }
}