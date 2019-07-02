package com.example.goo.test.Activity.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Like_Member;
import com.example.goo.test.Fragment.Home.HomeFragment;
import com.example.goo.test.Item.ListItem_Check_Like_Member;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Goo on 2018-05-11.
 */

public class Check_Like_Member  extends AppCompatActivity {
    int project_id;
    String project_username;
    String project_email;
    private android.support.v7.widget.Toolbar toolbar;
    private static final String SHOW_LIKE_MEMBER = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/show_check_like_member.php";

    List<ListItem_Check_Like_Member> listItems;
    RecyclerView recyclerView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_check_like_member);



        //툴바
        //툴바
        toolbar = findViewById(R.id.toolbar);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("추천한 사람들");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


                getSupportFragmentManager().beginTransaction().replace(R.id.frame_like,new HomeFragment()).commit();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(Check_Like_Member.this));
        recyclerView.setHasFixedSize(true);


        //프로젝트 신청인원을 눌렀을 때 보내준 값을 가지고 옴.
        Intent intent = getIntent();
        project_id = intent.getIntExtra("project_id",0);
        project_username = intent.getStringExtra("username");
        project_email = intent.getStringExtra("email");

        //신청한 인원에 대한 arraylist 생성
        listItems = new ArrayList<ListItem_Check_Like_Member>();
        uploadLikeMember();

    }
    //신청인원 업로드
    public void uploadLikeMember(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_LIKE_MEMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("project_like");
                            // JSONObject data = jsonarray.getJSONObject(0);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                ListItem_Check_Like_Member item = new ListItem_Check_Like_Member();
                                    System.out.println("신청한놈들은 : " + object.getString("like_username"));
                                item.username = object.getString("like_username");
                                item.url = object.getString("profile");
                                listItems.add(item);


                            }

                            RecyclerViewAdapter_Show_Like_Member adapter = new RecyclerViewAdapter_Show_Like_Member(Check_Like_Member.this, listItems);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Check_Like_Member.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Check_Like_Member.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                System.out.println(project_email);
                System.out.println(project_username);
                System.out.println(project_id);
                params.put("like_email", project_email);
                params.put("like_username", project_username);
                params.put("like_id", String.valueOf(project_id));

                return params;

            }
        };

        MySingleton.getInstance(Check_Like_Member.this).addToRequestque(stringRequest);

    }
}