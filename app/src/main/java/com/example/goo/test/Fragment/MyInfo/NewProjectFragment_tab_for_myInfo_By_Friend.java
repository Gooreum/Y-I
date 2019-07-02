package com.example.goo.test.Fragment.MyInfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class NewProjectFragment_tab_for_myInfo_By_Friend extends Fragment {
    // Required empty public constructor
    public NewProjectFragment_tab_for_myInfo_By_Friend() {

    }
    private static final String SHOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_project.php";
    public static final String KEY_EMAIL = "email";
    List<ListItem_Show_Project> listItems;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    RecyclerView recyclerView3;



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


        //팔로잉 친구 arraylist 생성
        listItems = new ArrayList<ListItem_Show_Project>();

        //모집중인 프로젝트 업로드
        uploadProject();




        return rootView;
    }

 /*   @Override
    public void onResume() {
        Log.d(this.getClass().getSimpleName(), "onResume()");
        super.onResume();
        uploadProject();
        System.out.println("업데이트 함");

    }*/

    //프로젝트 업로드
    public void uploadProject(){
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
                                item.id = object.getInt("id");
                                item.title = object.getString("title");
                                item.content = object.getString("content");
                                item.username = object.getString("username");
                                item.email = object.getString("email");
                                item.history = object.getString("history");
                                item.like_count = object.getInt("like_count");
                                item.join_count = object.getInt("join_count");
                                item.url = object.getString("profile");
                                item.selected_num = object.getInt("selected_num");
                                item.apply_duration = object.getString("apply_duration");
                                item.gps = object.getString("gps");
                                item.dev_duration = object.getString("dev_duration");
                                item.member_num = object.getInt("member_num");
                                if(item.email.equals(loadLoginEmail())){
                                    item.me = true;
                                }
                                System.out.println("url : " + item.url);
                                listItems.add(item);

                            }

                            RecyclerViewAdapter_Show_Project_in_MyInfo adapter = new RecyclerViewAdapter_Show_Project_in_MyInfo(getContext(), listItems);
                            RecyclerViewAdapter_Show_Project_in_MyInfo adapter2 = new RecyclerViewAdapter_Show_Project_in_MyInfo(getContext(), listItems);
                            RecyclerViewAdapter_Show_Project_in_MyInfo adapter3 = new RecyclerViewAdapter_Show_Project_in_MyInfo(getContext(), listItems);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
                            RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView2.setLayoutManager(layoutManager2);
                            recyclerView3.setLayoutManager(layoutManager3);
                            recyclerView.setAdapter(adapter);
                            recyclerView2.setAdapter(adapter2);
                            recyclerView3.setAdapter(adapter3);
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView2.setNestedScrollingEnabled(false);
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

                params.put("email_info", loadLoginEmail());

                return params;

            }
        };

        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("friend_email", MODE_PRIVATE);
        String email_from_login = sp.getString("friend_email", null);
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);

        return email_from_login;
    }

}