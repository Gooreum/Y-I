package com.example.goo.test.Fragment.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.goo.test.Activity.AddProjectActivity;
import com.example.goo.test.Fragment.Home.HomeFragment;
import com.example.goo.test.Fragment.Home.NewProjectFragment_tab;
import com.example.goo.test.Item.ListItem_Show_Project;
import com.example.goo.test.R;
import com.example.goo.test.Util.Pager;
import com.example.goo.test.Util.Pager_Chat;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-03.
 */

public class ChatFragment extends Fragment implements View.OnClickListener{
    // Required empty public constructor
    public ChatFragment() {

    }
    private static final String SHOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_project.php";
    public static final String KEY_EMAIL = "email";
    List<ListItem_Show_Project> listItems;
    RecyclerView recyclerView;
    public static FloatingActionButton fab;


    //툴바
    private android.support.v7.widget.Toolbar toolBar;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        toolBar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);

        //플로팅 액션 버튼 여기서 친구 추가 밑 그룹채팅을 만들 수 있다.
        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);


        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        mTabLayout.addTab(mTabLayout.newTab().setText("친구목록"));
        mTabLayout.addTab(mTabLayout.newTab().setText("채팅방"));

        String strColor = "#ffffff";
        mTabLayout.setTabTextColors(Color.parseColor(strColor),Color.parseColor(strColor));

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // mSelectionsPagerAdapter = new SectionsPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());

        mViewPager = (ViewPager)rootView.findViewById(R.id.container);

        Pager_Chat adapter = new Pager_Chat(getFragmentManager(), mTabLayout.getTabCount());
        //Pager adapter = new Pager(getChildFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setScrollPosition(position, 0 , true);
                mTabLayout.setSelected(true);

                if(position == 0){
                    toolBar.getMenu().clear();
                    toolBar.inflateMenu(R.menu.menu_chat_friend_list);
                }else if(position == 1){
                    toolBar.getMenu().clear();
                    toolBar.inflateMenu(R.menu.menu_chat_chatting_room);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





        return rootView;
    }

    //프로젝트 모집글 추가 버튼 메서드
    public void add_fab(){

        Intent intent = new Intent(getContext(), AddProjectActivity.class);
        startActivity(intent);

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
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);


        return email_from_login;
    }
}
