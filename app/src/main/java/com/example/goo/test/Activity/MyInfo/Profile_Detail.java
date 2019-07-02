package com.example.goo.test.Activity.MyInfo;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.example.goo.test.Adapter.Profile_Adapter;
import com.example.goo.test.Fragment.MyInfo.MyInfoFragment;
import com.example.goo.test.Item.ListItem_Profile_Category;
import com.example.goo.test.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Goo on 2018-05-27.
 */

public class Profile_Detail extends AppCompatActivity implements View.OnClickListener {


    private ExpandableListView expLV;
    private Profile_Adapter adapter;
    private ImageView btn_back;
    ArrayList<String> listCategorias;

    ListItem_Profile_Category item_child;
    private Map<String, ArrayList<ListItem_Profile_Category>> mapChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        expLV = (ExpandableListView) findViewById(R.id.expLV);


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        listCategorias = new ArrayList<>();
        mapChild = new HashMap<>();
        cargarDatos();
        changeArrow();
    }

    private void cargarDatos() {

        ArrayList<String> intro = new ArrayList<>();
        ArrayList<String> profile = new ArrayList<>();
        ArrayList<String> my_skill = new ArrayList<>();
        ArrayList<String> experience = new ArrayList<>();

        ArrayList<ListItem_Profile_Category> list_child = new ArrayList<>();
        ArrayList<ListItem_Profile_Category> list_child2 = new ArrayList<>();
        ArrayList<ListItem_Profile_Category> list_child3 = new ArrayList<>();
        ArrayList<ListItem_Profile_Category> list_child4 = new ArrayList<>();
        ListItem_Profile_Category item_child = new ListItem_Profile_Category();
        ListItem_Profile_Category item_child2 = new ListItem_Profile_Category();
        ListItem_Profile_Category item_child3 = new ListItem_Profile_Category();
        ListItem_Profile_Category item_child4 = new ListItem_Profile_Category();
        ListItem_Profile_Category item_child5 = new ListItem_Profile_Category();


        listCategorias.add("프로필");
        listCategorias.add("경력");
        listCategorias.add("보유기술");
        listCategorias.add("이력");

        //개인정보
        item_child.child_name ="개인정보";
        item_child.child_content ="프로필 사진,닉네임,비밀번호를 변경할 수 있습니다.";
        item_child.isClicked = false;
        item_child.img = R.drawable.ic_subject_black_24dp;
        //소개
        item_child5.child_name ="간단 프로필";
        item_child5.child_content ="회원님이 관심을 두는 분야를 간단 프로필에서 간략히 소개하세요.";
        item_child5.isClicked = false;
        item_child5.img = R.drawable.ic_subject_black_24dp;


        //프로필-경력
        item_child2.child_name ="경력";
        item_child2.child_content ="지금까지의 경력을 소개해주세요.";
        item_child2.isClicked = false;
        item_child2.img = R.drawable.ic_card_travel_black_24dp;
        //보유기술-보유기술
        item_child3.child_name ="보유기술";
        item_child3.child_content ="자신이 사용할 수 있는 기술";
        item_child3.isClicked = false;
        item_child3.img = R.drawable.ic_pan_tool_black_24dp;
        //프로젝트
        item_child4.child_name ="프로젝트";
        item_child4.child_content ="지금까지 해온 프로젝트를 소개해주세요.";
        item_child4.isClicked = false;
        item_child4.img = R.drawable.ic_content_paste_black_24dp;


        list_child.add(item_child);
        list_child.add(item_child5);
        list_child2.add(item_child2);
        list_child3.add(item_child3);
        list_child4.add(item_child4);


        intro.add("abc");
        profile.add("def");
        my_skill.add("ghi");
        experience.add("jkl");



       /* mapChild.put(listCategorias.get(0), intro);
        mapChild.put(listCategorias.get(1), profile);
        mapChild.put(listCategorias.get(2), my_skill);
        mapChild.put(listCategorias.get(3), experience);
        */
        mapChild.put(listCategorias.get(0), list_child);
        mapChild.put(listCategorias.get(1), list_child2);
        mapChild.put(listCategorias.get(2), list_child3);
        mapChild.put(listCategorias.get(3), list_child4);

        adapter = new Profile_Adapter(listCategorias, mapChild, this);
        expLV.setAdapter(adapter);
    }
    //리스트뷰 화살표 오른쪽으로 옮기기
    public void changeArrow() {
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50, r.getDisplayMetrics());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expLV.setIndicatorBounds(width - px, width);
        } else {
            expLV.setIndicatorBoundsRelative(width - px, width);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_back:
                finish();

                getSupportFragmentManager().beginTransaction().replace(R.id.main,new MyInfoFragment()).commit();

        }
    }
}