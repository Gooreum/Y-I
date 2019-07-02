package com.example.goo.test.Util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.goo.test.Fragment.Home.NewProjectFragment_tab;
import com.example.goo.test.Fragment.MyInfo.MyInfo_tab_profile;
import com.example.goo.test.Fragment.MyInfo.NewProjectFragment_tab_for_myInfo;
import com.example.goo.test.Fragment.Tab2Fragment;
import com.example.goo.test.Fragment.Tab3Fragment;

/**
 * Created by Goo on 2018-05-08.
 */

public class Pager_MyInfo extends FragmentStatePagerAdapter {
    int tabCount;

    public Pager_MyInfo(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyInfo_tab_profile tab2Fragment = new MyInfo_tab_profile();
                return tab2Fragment;

            case 1:

                NewProjectFragment_tab_for_myInfo tab1Fragment = new NewProjectFragment_tab_for_myInfo();
                return tab1Fragment;
            case 2:
                Tab3Fragment tab3Fragment = new Tab3Fragment();
                return tab3Fragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
