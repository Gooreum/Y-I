package com.example.goo.test.Fragment.MyInfo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.goo.test.Activity.LoginActivity;
import com.example.goo.test.R;
import com.example.goo.test.Util.Pager_MyInfo;
import com.example.goo.test.Util.Pager_MyInfo_By_Friend;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-03.
 */

public class MyInfoFragment_By_Friend extends Fragment  {

    LoginActivity loginActivity;

    private DrawerLayout drawer;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    //마지막으로 뒤로가기 버튼이 터치된 시간
    private long lastTimeBackPressed;
    public String email_friend;

    public MyInfoFragment_By_Friend() {

    }

    public static final String KEY_EMAIL = "email";
    Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myinfo, container, false);
        loginActivity = new LoginActivity();



        //친구 이메일 값 가져오기
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            email_friend = bundle.getString("email");

        }


        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        mTabLayout.addTab(mTabLayout.newTab().setText("프로필"));
        mTabLayout.addTab(mTabLayout.newTab().setText("프로젝트"));

        String strColor = "#ffffff";
        mTabLayout.setTabTextColors(Color.parseColor(strColor),Color.parseColor(strColor));

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // mSelectionsPagerAdapter = new SectionsPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());

        mViewPager = (ViewPager) rootView.findViewById(R.id.container);

        Pager_MyInfo_By_Friend adapter = new Pager_MyInfo_By_Friend(getFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setScrollPosition(position, 0, true);
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
    //툴바설정
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu_for_myinfo, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("friend_email", MODE_PRIVATE);
        String email_from_login = sp.getString("friend_email", null);
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);

        return email_from_login;
    }


}
