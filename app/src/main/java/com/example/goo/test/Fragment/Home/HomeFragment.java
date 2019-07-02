package com.example.goo.test.Fragment.Home;

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
import com.example.goo.test.R;
import com.example.goo.test.Item.ListItem_Show_Project;
import com.example.goo.test.Util.Pager;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-03.
 * 홈화면에서는 팔로잉한 친구들의 프로젝트 게시글들을 볼 수 있다.
 */

public class HomeFragment extends Fragment  implements View.OnClickListener{
    // Required empty public constructor
    public HomeFragment() {

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


        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        toolBar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);


        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);


        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        mTabLayout.addTab(mTabLayout.newTab().setText("모집중인 프로젝트"));
        mTabLayout.addTab(mTabLayout.newTab().setText("진행중인 프로젝트"));
        mTabLayout.addTab(mTabLayout.newTab().setText("완료된 프로젝트 "));
        String strColor = "#ffffff";
         mTabLayout.setTabTextColors(Color.parseColor(strColor),Color.parseColor(strColor));

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

       // mSelectionsPagerAdapter = new SectionsPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());

        mViewPager = (ViewPager)rootView.findViewById(R.id.container);

        Pager adapter = new Pager(getFragmentManager(), mTabLayout.getTabCount());
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


