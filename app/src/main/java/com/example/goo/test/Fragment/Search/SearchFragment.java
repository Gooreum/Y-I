package com.example.goo.test.Fragment.Search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Adapter.RecyclerViewAdapter_friendSearch_Recommend;
import com.example.goo.test.Adapter.RecyclerViewAdapter_frinedSearch;
import com.example.goo.test.Item.ListItem_friendSearch;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-03.
 */

public class SearchFragment extends Fragment implements android.support.v7.widget.SearchView.OnQueryTextListener {

    private static final String URL_DATA = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/friendView.php";
    private static final String REC_FRIEND = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/recommended_friend.php";
    private LinearLayout line,line_result;
    //친구 검색리스트 만들기
    List<ListItem_friendSearch> listItems;
    List<ListItem_friendSearch> listItems_rec;
    RecyclerView recyclerView;
    RecyclerView recycler_view_recommend_friend;
    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //툴바
    private Toolbar toolBar;

    public SearchFragment() {

    }

    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        line = rootView.findViewById(R.id.line);
        line_result = rootView.findViewById(R.id.line_result);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recycler_view_recommend_friend = rootView.findViewById(R.id.recycler_view_recommend_friend);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_recommend_friend.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recycler_view_recommend_friend.setHasFixedSize(true);

        //친구 검색 arraylist 생성
        listItems = new ArrayList<ListItem_friendSearch>();
        listItems_rec = new ArrayList<ListItem_friendSearch>();


        //toolbar initialize
        toolBar = rootView.findViewById(R.id.toolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);
        setHasOptionsMenu(true);

        uploadRecommendedFriend();
        return rootView;
    }


    //액션바에 검색버튼 넣어주기. 메뉴 카테고리에서 만든 메뉴를 가져옴.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {

                //Toast.makeText(getContext(), "Action View Expanded..", Toast.LENGTH_SHORT).show();

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                //Toast.makeText(getContext(), "Action View Collapsed..", Toast.LENGTH_SHORT).show();

                //검색바에서 뒤로가기 버튼을 누르면 리사이클러뷰의 결과가 사라진다.
                listItems.clear();
                line.setVisibility(View.VISIBLE);
                line_result.setVisibility(View.GONE);
                return true;
            }
        };

        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setOnActionExpandListener(onActionExpandListener);

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }


    //검색바에서 enter를 입력해야지만 검색어가 서버로 전송된다.
    @Override
    public boolean onQueryTextSubmit(String s) {

        final String userInput = s.toLowerCase();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("member");

                            for (int i = 0; i < jsonarray.length()-1; i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                ListItem_friendSearch item = new ListItem_friendSearch();
                                item.username = object.getString("username");
                                item.email = object.getString("email");
                                item.url = object.getString("profile");
                                //검색 전에 검색 리스트아이템에 있는 값들을 삭제해준다.
                                //--> 이렇게 해주지 않으면 검색 결과들이 계속해서 리스트에 쌓이게 됨.
                                listItems.clear();

                                //검색 결과를 리스트에 추가한다.
                                listItems.add(item);

                            }

                            RecyclerViewAdapter_frinedSearch adapter = new RecyclerViewAdapter_frinedSearch(getContext(), listItems);
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

                params.put("userInput", userInput);

                return params;

            }
        };

        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);
        //검색결과가 나오면 친구추천 카테고리는 사리지게 만듦.
        line.setVisibility(View.GONE);
        line_result.setVisibility(View.VISIBLE);
        return true;
    }

    //onQueryTextChange 메서드는 엔터를 누르지 않아도 입력하는 순간 그 값들이 서버로 전송됨.
    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }


    //추천친구 받아오기
    public void uploadRecommendedFriend(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REC_FRIEND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("recommend");

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                ListItem_friendSearch item = new ListItem_friendSearch();
                                item.username = object.getString("iam");
                                item.email = object.getString("email");
                                item.url = object.getString("iam_profile");
                                System.out.println("추천친구: " + item.username);
                                //검색 결과를 리스트에 추가한다.
                                listItems_rec.add(item);

                            }
                            RecyclerViewAdapter_friendSearch_Recommend adapter = new RecyclerViewAdapter_friendSearch_Recommend(getContext(), listItems_rec);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                            recycler_view_recommend_friend.setLayoutManager(layoutManager);
                            recycler_view_recommend_friend.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);


        return email_from_login;
    }
    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
