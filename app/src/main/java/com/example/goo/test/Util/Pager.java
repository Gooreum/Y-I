package com.example.goo.test.Util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.goo.test.Fragment.Home.CompletedProjectFragment_tab;
import com.example.goo.test.Fragment.Home.DevelopingProjectFragment_tab;
import com.example.goo.test.Fragment.Home.NewProjectFragment_tab;
import com.example.goo.test.Fragment.Tab2Fragment;
import com.example.goo.test.Fragment.Tab3Fragment;

/**
 * Created by Goo on 2018-05-08.
 */

public class Pager extends FragmentStatePagerAdapter {
    int tabCount;

    public Pager(FragmentManager fm, int tabCount){
        super(fm);
        this.tabCount = tabCount;

    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                NewProjectFragment_tab tab1Fragment = new NewProjectFragment_tab();
                return tab1Fragment;

            case 1 :
                DevelopingProjectFragment_tab tab2Fragment = new DevelopingProjectFragment_tab();
                return tab2Fragment;

            case 2:
                CompletedProjectFragment_tab tab3Fragment = new CompletedProjectFragment_tab();
                return tab3Fragment;
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }

    //이코드만 넣어줬는데 댓글 입력후 프래그먼트가 새로고침 되었다.
    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }


}
