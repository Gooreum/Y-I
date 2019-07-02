package com.example.goo.test.Fragment.Home;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Developing_Project;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Project;
import com.example.goo.test.Item.ListItem_Show_Project;
import com.example.goo.test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.goo.test.Fragment.Home.HomeFragment.fab;

/**
 * Created by Goo on 2018-05-03.
 * 홈화면에서는 팔로잉한 친구들의 프로젝트 게시글들을 볼 수 있다.
 */

public class DevelopingProjectFragment_tab extends Fragment {
    // Required empty public constructor
    public DevelopingProjectFragment_tab() {

    }


    //마지막으로 뒤로가기 버튼이 터치된 시간
    private long lastTimeBackPressed;

    private static final String SHOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_project.php";
    private static final String PROJECT_IMAGE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_project_image.php";

    List<ListItem_Show_Project> listItems;
    RecyclerView recyclerView;


    //스와이프 레이아웃
    SwipeRefreshLayout swipe_refresh;
    //툴바
    private android.support.v7.widget.Toolbar toolBar;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    StringRequest stringRequest;
    StringRequest stringRequest2;
    String image;

    Map<String, String> params2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_new_project, container, false);


        toolBar = rootView.findViewById(R.id.toolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);


        //스와이프
        swipe_refresh = rootView.findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        //refreshLayout.setRefreshing(false);
        mTabLayout = rootView.findViewById(R.id.tabs);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        //팔로잉 친구 arraylist 생성
        listItems = new ArrayList<ListItem_Show_Project>();

        //플로팅액션버튼이 리사이클러뷰가 돌아가는 동안에는 숨겨졌다가 멈추면 다시 나타남.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        //모집중인 프로젝트 업로드
        uploadProject();
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG,"onActivityCreated");
        super.onActivityCreated(savedInstanceState);


    }
    public void onStart(){
        super.onStart();

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("----onResume()---","_---NewProject_fragment_tab_onResume()----_");
       // uploadProject();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("----onDestroyView---","_---onDestroyView----_");
    }

    //프로젝트 업로드
    public void uploadProject() {

         stringRequest = new StringRequest(Request.Method.POST, SHOW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("project_content");



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


                                listItems.add(item);

                            }

                            RecyclerViewAdapter_Show_Developing_Project adapter = new RecyclerViewAdapter_Show_Developing_Project(getContext(), listItems);
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email_developing", loadLoginEmail());

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

       // MySingleton.getInstance(getContext()).addToRequestque(stringRequest);
       // CustomVolleyRequest.getInstance(getContext()).addToRequestque(stringRequest);
        //CustomVolleyRequest.getInstance(getContext()).getRequestQueue();
    }

    //다른 탭에서 넘어 왔을 때 새로고침을 해줌.
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    /*  public void add_fab(){

          Bundle bundle = new Bundle();
          AddProjectFragment addProjectFragment = new AddProjectFragment();
          addProjectFragment.setArguments(bundle);

          ((AppCompatActivity) getContext()).getSupportFragmentManager()
                  .beginTransaction()
                  //뒤로가기 눌렀을 때 다시 여기 화면으로 돌아오기 위해선 addtoBackStack메서드를 사용.
                  .replace(R.id.fragment_container, addProjectFragment).addToBackStack(null)
                  .commit();

      }

      //클릭 이벤트 모음.
      @Override
      public void onClick(View view) {
          switch (view.getId()) {
              //플로팅 액션바 버튼을 누르면 프로젝트 모집글 추가 화면이 뜬다.
              case R.id.fab:
                  add_fab();

                  break;
          }
      }*/


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


