package com.example.goo.test.Fragment.MyInfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.goo.test.Adapter.RecyclerViewAdapter_Following;

import com.example.goo.test.Item.ListItem_friendSearch;
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
 * Created by Goo on 2018-05-06.
 * 여기서는 MyInfo에 있는 화면의 팔로잉 수를 눌렀을 경우
 * 팔로잉 하는 사람들의 목록을 보여주고 관리할 수 있는 프래그먼트 클래스이다.
 */

public class   Following_Fragment extends Fragment {
    //친구 검색리스트 만들기
    List<ListItem_friendSearch> listItems;
    RecyclerView recyclerView;


    public Following_Fragment(){

    }

    private static final String FOLLOWING_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/followCount.php";
    private static final String FOLLOWER_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/followerCount.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_following, container, false);


        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);


        //팔로잉 친구 arraylist 생성
        listItems = new ArrayList<ListItem_friendSearch>();
        getFollowingList();
        return rootView;
    }

    public void getFollowingList(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FOLLOWING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("follow");
                           // JSONObject data = jsonarray.getJSONObject(0);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                ListItem_friendSearch item = new ListItem_friendSearch();
                                item.username = object.getString("you");
                                item.url = object.getString("profile");
                               // item.btn_follow = "팔로우 취소";
                                //검색 결과를 리스트에 추가한다.
                                listItems.add(item);

                            }

                            RecyclerViewAdapter_Following adapter = new RecyclerViewAdapter_Following(getContext(), listItems);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
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
}
