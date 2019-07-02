package com.example.goo.test.Fragment.Alarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.goo.test.Adapter.RecyclerViewAdapter_Alarm;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Project;
import com.example.goo.test.Fragment.Home.HomeFragment;
import com.example.goo.test.Item.Item_alarm;
import com.example.goo.test.Item.Item_child;
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
 * Created by Goo on 2018-06-25.
 */

public class AlarmFragment extends Fragment {
    private static final String GET_ALARM_MESSAGES = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_alarm_messages.php";


    public AlarmFragment(){

    }
    RecyclerView recycler_view;

    List<Item_alarm> list;

    private SwipeRefreshLayout swipe_refresh;
    private android.support.v7.widget.Toolbar toolbar;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);

        //리스트 생성
        list = new ArrayList<>();
        //리사이클러뷰 생성
        recycler_view = rootView.findViewById(R.id.recycler_view);

        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setHasFixedSize(true);


        swipe_refresh = rootView.findViewById(R.id.swipe_refresh);
        //위로 당기면 스와이프 레이아웃에 의해 새로고침이 됨.
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        swipe_refresh.setRefreshing(false);
        //툴바
        toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("알림");
        toolbar.setTitleTextColor(0xFFFFFFFF);

        uploadAlarm();
        return rootView;

    }
    //알림 메세지 받기
    public void uploadAlarm(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_ALARM_MESSAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("alarm");
                            for (int k = 0; k < jsonarray.length(); k++) {

                                JSONObject object = jsonarray.getJSONObject(k);
                                Item_alarm item = new Item_alarm();
                                //유저이름, 프로필 이미지, 이메일 값
                                item.username = object.getString("username");
                                item.profile = object.getString("profile");
                                item.email = object.getString("email");
                                item.history = object.getString("history");
                                item.project_email = object.getString("project_email");
                                //새로운 모집글 작성 여부와 게시글 번호
                                item.new_project = object.getInt("new_project");
                                item.new_project_id = object.getInt("new_project_id");

                                //프로젝트 시작여부 및 프로젝트 번호
                                item.project_start = object.getInt("project_start");
                                item.project_start_id = object.getInt("project_start_id");

                                //프로젝트 추가모집 여부 및 프로젝트 번호
                                item.project_recruiting = object.getInt("project_recruiting");
                                item.project_recruiting_id = object.getInt("project_recruiting_id");
                                item.cancel_more_recruiting = object.getInt("cancel_more_recruiting");

                                //프로젝트 완료 여부 및 프로젝트 번호
                                item.project_completed = object.getInt("project_completed");
                                item.project_completed_id = object.getInt("project_completed_id");
                                item.project_url = object.getString("project_url");

                                //프로젝트 좋아요 여부 및 프로젝트 번호, 프로젝트 작성자명
                                item.project_like = object.getInt("project_like");
                                item.project_like_id = object.getInt("project_like_id");
                                item.project_like_writer = object.getString("project_like_project_writer");

                                //프로젝트 신청하기 여부 및 프로젝트 번호, 프로젝트 작성자명
                                item.project_join = object.getInt("project_join");
                                item.project_join_id = object.getInt("project_join_id");
                                item.project_join_writer = object.getString("project_join_project_writer");

                                //프로젝트 공유 여부 및 프로젝트 번호, 프로젝트 작성자명
                                item.project_share = object.getInt("project_share");
                                item.project_share_id = object.getInt("project_share_id");
                                item.project_share_writer = object.getString("project_share_writer");

                                //내 프로젝트 댓글 여부 및 프로젝트 번호
                                item.project_reply = object.getInt("project_reply");
                                item.project_reply_id = object.getInt("project_reply_id");

                                //내 프로젝트 대댓글 여부 , 댓글 작성자 이름 및 프로젝트 번호
                                item.project_re_reply = object.getInt("project_re_reply");
                                item.project_re_reply_id = object.getInt("project_re_reply_id");
                                item.project_re_reply_writer = object.getString("project_re_reply_writer");

                                list.add(item);
                            }
                            RecyclerViewAdapter_Alarm adapter = new RecyclerViewAdapter_Alarm(getContext(), list);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recycler_view.setLayoutManager(layoutManager);
                            recycler_view.setAdapter(adapter);

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

        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }
    public void refresh() {
        //전송 후 프래그먼트를 띄었다가 다시 붙임.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}