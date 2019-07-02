package com.example.goo.test.Fragment.MyInfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Completed_Project;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Developing_Project;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Project;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Project_in_MyInfo;
import com.example.goo.test.Item.ListItem_Show_Project;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-08.
 */

public class NewProjectFragment_tab_for_myInfo extends Fragment {
    // Required empty public constructor
    public NewProjectFragment_tab_for_myInfo() {

    }
    private static final String SHOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_project.php";
    private static final String SHOW_URL_DEVELOPING = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_project.php";
    private static final String SHOW_URL_COMPLETED = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_project.php";
    public static final String KEY_EMAIL = "email";
    List<ListItem_Show_Project> listItems;
    List<ListItem_Show_Project> listItems_developing;
    List<ListItem_Show_Project> listItems_completed;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    RecyclerView recyclerView3;
    //스와이프 레이아웃
    SwipeRefreshLayout swipe_refresh;
    //프로젝트 갯수
    private TextView new_num,projecting_num,complete_num;


    //private SectionsPagerAdapter mSelectionsPagerAdapter;
    //툴바
    private android.support.v7.widget.Toolbar toolBar;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_new_project_for_myinfo, container, false);

        toolBar = rootView.findViewById(R.id.toolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);

        mTabLayout = rootView.findViewById(R.id.tabs);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView2 = rootView.findViewById(R.id.recycler_view2);
        recyclerView3 = rootView.findViewById(R.id.recycler_view3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        recyclerView3.setHasFixedSize(true);

        //프로젝트 갯수
        new_num = rootView.findViewById(R.id.ing_num);
        projecting_num = rootView.findViewById(R.id.projecting_num);
        complete_num = rootView.findViewById(R.id.complete_num);

        //스와이프
        swipe_refresh = rootView.findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        //팔로잉 친구 arraylist 생성
        listItems = new ArrayList<ListItem_Show_Project>();
        listItems_developing = new ArrayList<ListItem_Show_Project>();
        listItems_completed = new ArrayList<ListItem_Show_Project>();

        //모집중인 프로젝트 업로드
        uploadProject_new();
        //현재 진행중인 프로젝트 불러오기
        uploadProject_ing();
        //완료된 프로젝트 불러오기
       uploadProject_completed();




        return rootView;
    }


//프로젝트 업로드
public void uploadProject_new(){
    StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonobject = new JSONObject(response);
                        JSONArray jsonarray = jsonobject.getJSONArray("project_content");
                        // JSONObject data = jsonarray.getJSONObject(0);

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject object = jsonarray.getJSONObject(i);

                            ListItem_Show_Project item = new ListItem_Show_Project();


                            item.history = object.getString("history");

                            item.title = object.getString("title");

                            item.content = object.getString("content");
                            item.username = object.getString("username");
                            item.email = object.getString("email");
                            item.id = object.getInt("id");
                            item.like_count = object.getInt("like_count");
                            item.reply_count = object.getInt("reply_count");
                            item.join_count = object.getInt("join_count");
                            item.url = object.getString("profile");
                            item.selected_num = object.getInt("selected_num");
                            item.apply_duration = object.getString("apply_duration");
                            item.gps = object.getString("gps");
                            item.dev_duration = object.getString("dev_duration");
                            item.member_num = object.getInt("member_num");
                            item.img_count = object.getInt("img_count");
                            item.img_url1 = object.getString("img1");
                            item.img_url2 = object.getString("img2");
                            item.img_url3 = object.getString("img3");
                            item.img_url4 = object.getString("img4");
                            item.img_url5 = object.getString("img5");
                            item.amount = object.getInt("amount");

                            item.project_developing = object.getInt("project_developing");
                            item.more_recruit_member = object.getInt("more_recruit_member");
                            item.project_recruting = object.getInt("project_recruting");

                            System.out.println("url  1  값은 " + item.img_url1);
                            System.out.println("url  2  값은 " + item.img_url2);
                            System.out.println("url  3  값은 " + item.img_url3);
                            System.out.println("url  4  값은 " + item.img_url4);
                            System.out.println("url  5  값은 " + item.img_url5);

                            if (item.email.equals(loadLoginEmail())) {
                                item.me = true;
                            }
                            new_num.setText(jsonarray.length() +"");

                            listItems.add(item);

                        }

                        RecyclerViewAdapter_Show_Project adapter = new RecyclerViewAdapter_Show_Project(getContext(), listItems);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setNestedScrollingEnabled(false);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

        //해쉬맵을 통해 php에 이메일 값을 보내줌.
        @Override
        protected Map<String, String> getParams() {

            Map<String, String> params = new HashMap<String, String>();

            params.put("email_info", loadLoginEmail());

            return params;

        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
    requestQueue.add(stringRequest);
}

    //진행중인 프로젝트 업로드
    public void uploadProject_ing(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_URL_DEVELOPING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("project_content");
                            // JSONObject data = jsonarray.getJSONObject(0);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                ListItem_Show_Project item = new ListItem_Show_Project();


                                item.history = object.getString("history");

                                item.title = object.getString("title");

                                item.content = object.getString("content");
                                item.username = object.getString("username");
                                item.email = object.getString("email");
                                item.id = object.getInt("id");
                                item.like_count = object.getInt("like_count");
                                item.reply_count = object.getInt("reply_count");
                                item.join_count = object.getInt("join_count");
                                item.url = object.getString("profile");
                                item.selected_num = object.getInt("selected_num");
                                item.apply_duration = object.getString("apply_duration");
                                item.gps = object.getString("gps");
                                item.dev_duration = object.getString("dev_duration");
                                item.member_num = object.getInt("member_num");
                                item.img_count = object.getInt("img_count");
                                item.img_url1 = object.getString("img1");
                                item.img_url2 = object.getString("img2");
                                item.img_url3 = object.getString("img3");
                                item.img_url4 = object.getString("img4");
                                item.img_url5 = object.getString("img5");


                                item.project_recruting = object.getInt("project_recruting");
                                item.more_recruit_member = object.getInt("more_recruit_member");
                                item.project_developing = object.getInt("project_developing");




                                System.out.println("지금 상태는 말이야 : " + object.getInt("project_recruting"));
                                if (item.email.equals(loadLoginEmail())) {
                                    item.me = true;
                                }
                                projecting_num.setText(jsonarray.length() + "");

                                listItems_developing.add(item);

                            }




                            RecyclerViewAdapter_Show_Developing_Project adapter2 = new RecyclerViewAdapter_Show_Developing_Project(getContext(), listItems_developing);
                            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
                            recyclerView2.setAdapter(adapter2);
                            recyclerView2.setLayoutManager(layoutManager2);
                            recyclerView2.setNestedScrollingEnabled(false);






                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email_ing", loadLoginEmail());

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    //완료된 프로젝트 업로드
    public void uploadProject_completed(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_URL_COMPLETED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("project_content");
                            // JSONObject data = jsonarray.getJSONObject(0);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                ListItem_Show_Project item = new ListItem_Show_Project();


                                item.history = object.getString("history");

                                item.title = object.getString("title");

                                item.content = object.getString("content");
                                item.username = object.getString("username");
                                item.email = object.getString("email");
                                item.id = object.getInt("id");
                                item.like_count = object.getInt("like_count");
                                item.reply_count = object.getInt("reply_count");
                                item.join_count = object.getInt("join_count");
                                item.url = object.getString("profile");
                                item.selected_num = object.getInt("selected_num");
                                item.apply_duration = object.getString("apply_duration");
                                item.gps = object.getString("gps");
                                item.dev_duration = object.getString("dev_duration");
                                item.member_num = object.getInt("member_num");
                                item.img_count = object.getInt("img_count");
                                item.amount = object.getInt("amount");

                                item.project_url = object.getString("project_url");
                                item.project_completed = object.getInt("project_completed");


                                if (item.email.equals(loadLoginEmail())) {
                                    item.me = true;
                                }
                                complete_num.setText(jsonarray.length() + "");

                                listItems_completed.add(item);

                            }


                            RecyclerViewAdapter_Show_Completed_Project adapter3 = new RecyclerViewAdapter_Show_Completed_Project(getContext(), listItems_completed);
                            RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getContext());
                            recyclerView3.setAdapter(adapter3);
                            recyclerView3.setLayoutManager(layoutManager3);
                            recyclerView3.setNestedScrollingEnabled(false);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email_my_project_completed", loadLoginEmail());

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    public void refresh(){
        //전송 후 프래그먼트를 띄었다가 다시 붙임.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);


        return email_from_login;
    }


}