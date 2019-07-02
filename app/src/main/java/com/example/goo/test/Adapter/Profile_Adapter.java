package com.example.goo.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goo.test.Activity.MyInfo.CareerActivity;
import com.example.goo.test.Activity.MyInfo.Interested_section_activity;
import com.example.goo.test.Activity.LoginActivity;
import com.example.goo.test.Activity.MyInfo.MySkillActivity;
import com.example.goo.test.Activity.MyInfo.ProfileActivity;
import com.example.goo.test.Item.Item;
import com.example.goo.test.Item.ListItem_Profile_Category;
import com.example.goo.test.R;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-27.
 */


public class Profile_Adapter extends BaseExpandableListAdapter {

    private static final String ADD_Re_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/add_re_reply.php";

    private ArrayList<String> listCategoria;
    private Map<String, ArrayList<ListItem_Profile_Category>> mapChild;
    //private Map<String, ArrayList<String>> mapChild;
    private Context context;


    Item items;
    ListItem_Profile_Category items_child;

    TextView txt_message, reply_count;
    TextView txt_username;
    TextView txt_reply;

    TextView txt_message2, txt_reply2;
    TextView txt_username2;
    CircleImageView reply_img2;


    public Profile_Adapter(ArrayList<String> listCategoria, Map<String, ArrayList<ListItem_Profile_Category>> mapChild, Context context) {
        this.listCategoria = listCategoria;
        this.mapChild = mapChild;
        this.context = context;
    }


    @Override
    public int getGroupCount() {
        return this.listCategoria.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mapChild.get(listCategoria.get(groupPosition)).size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return listCategoria.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mapChild.get(listCategoria.get(groupPosition)).get(childPosition);

    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listCategoria = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.profile_group, null);
        }
        TextView tvGroup = convertView.findViewById(R.id.group_name);
        tvGroup.setText(listCategoria);
        //items = listCategoria.get(groupPosition);


        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // String item = (String)getChild(groupPosition,childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.profile_child, null);
        }

        // items = listCategoria.get(groupPosition);
        items_child = (ListItem_Profile_Category) getChild(groupPosition, childPosition);
        ImageView img = convertView.findViewById(R.id.img);
        TextView child_name = convertView.findViewById(R.id.child_name);
        TextView child_content = convertView.findViewById(R.id.child_content);
        ImageView go_detail = convertView.findViewById(R.id.go_detail);
        img.setImageResource(items_child.img);

        child_name.setText(items_child.child_name);
        child_content.setText(items_child.child_content);



        //프로필 화면에서 각각의 자식 이름 값에 따라 해당 프로필 상세보기 화면으로 전환
        //간단프로필은 간단프로필 화면, 경력은 경력 화면 뭐 이런 식으로..
        switch (items_child.child_name) {
            case "간단 프로필":
                go_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        items_child.isClicked = true;
                        Toast.makeText(context, items_child.child_name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Interested_section_activity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "개인정보":
                go_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        items_child.isClicked = true;
                        Toast.makeText(context, items_child.child_name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "경력":
                go_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        items_child.isClicked = true;
                        Toast.makeText(context, items_child.child_name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, CareerActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "보유기술":
                go_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        items_child.isClicked = true;
                        Toast.makeText(context, items_child.child_name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MySkillActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "프로젝트":
                go_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        items_child.isClicked = true;
                        Toast.makeText(context, items_child.child_name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
        }

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = context.getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }
}
