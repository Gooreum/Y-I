package com.example.goo.test.Util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.goo.test.Fragment.Chat.Tab_Chat_Friend_List;
import com.example.goo.test.Fragment.Chat.Tab_Chat_Room_List;
import com.example.goo.test.Fragment.Home.HomeFragment;
import com.example.goo.test.Fragment.Tab2Fragment;
import com.example.goo.test.Fragment.Tab3Fragment;
import com.example.goo.test.R;

/**
 * Created by Goo on 2018-06-04.
 */

public class Pager_Chat extends FragmentStatePagerAdapter {
    int tabCount;

    public Pager_Chat(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Tab_Chat_Friend_List tab1Fragment = new Tab_Chat_Friend_List();
                return tab1Fragment;

            case 1:
                Tab_Chat_Room_List tab2Fragment = new Tab_Chat_Room_List();
                return tab2Fragment;

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

    //이코드만 넣어줬는데 댓글 입력후 프래그먼트가 새로고침 되었다.
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
