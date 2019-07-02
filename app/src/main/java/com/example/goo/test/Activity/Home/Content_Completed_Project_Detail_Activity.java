package com.example.goo.test.Activity.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Career;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Interesting;
import com.example.goo.test.Adapter.RecyclerViewAdapter_MySkill;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Recommend;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Reply;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Join_Member_Detail;
import com.example.goo.test.Fragment.Home.HomeFragment;
import com.example.goo.test.Item.Item;
import com.example.goo.test.Item.Item_Career;
import com.example.goo.test.Item.Item_Interesting;
import com.example.goo.test.Item.Item_MySkill;
import com.example.goo.test.Item.Item_Recommend;
import com.example.goo.test.Item.Item_child;
import com.example.goo.test.Item.ListItem_Check_Join_Member;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Goo on 2018-05-31.
 */

public class Content_Completed_Project_Detail_Activity extends AppCompatActivity implements View.OnClickListener {
    private static final String SHOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_project.php";
    private static final String SHOW_JOIN_MEMBER = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/show_check_join_member.php";
    private static final String SHOW_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_reply.php";
    private static final String SHOW_Re_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_re_reply.php";
    private static final String INTEREST_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_interest.php";
    private static final String CAREER_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_career.php";
    private static final String MYSKILL_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_myskill.php";
    private static final String RECOMMEND_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_recommend.php";
    public static int selected_count_detail;
    public static int join_count_detail;
    public static TextView txt_selected_num_detail;
    private TextView txt_member_num,txt_slash;

    public static String project_email_detail;
    public static int project_id_detail;

    public static Button btn_send, btn_re_send;
    public static EditText edit_reply;
    public static TextView text_reply_to;
    public static ImageView cancel;


    private ExpandableListView expLV;
    private RecyclerViewAdapter_Reply adapter;
    ArrayList<Item> listCategorias;
    Item_child item_child;
    private Map<Integer, ArrayList<Item_child>> mapChild;
    Item item;

    private TextView text_username, dev_duration, apply_duration, member_num, gps;
    private TextView text_email, text_title, text1, text2, text3, text4, text5;

    //private TextView text_content;
    private ReadMoreTextView text_content;
    private TextView text_history, profile_expand, profile_collapse,recommend;
    private TextView txtOptionDigit;
    private TextView txtOptionDigit_not_myproject;
    private LinearLayout line_join, line_pic;
    // private Button btn_join, ;
    private ImageView btn_like, btn_unlike, btn_reply, btn_join, btn_unjoin, btn_confirm_project, btn_project_completed, project_pic, project_pic2,
            project_img1, project_img2, project_img3, project_img4, project_img5;
    private ScrollView scrollView;
    private CircleImageView prof_pic, join_member_pic;
    private TextView txt_like, txt_join, txt_reply;
    private RelativeLayout line_texts;
    private RecyclerView recyclerView_join_member, recycler_view_skill, recycler_view_career, recycler_view_interest,recycler_view_recommend;
    Toolbar toolbar;
    RelativeLayout relative_profile;

    //리사이클러뷰 아이템에서 가지고 온 프로젝트 아이디 값과, 프로젝트 주인의 이메일 값
    int project_id;
    String project_email;


    List<ListItem_Check_Join_Member> listItems;
    List<Item_Interesting> list_interest;
    List<Item_Career> list_career;
    List<Item_MySkill> list_myskill;
    List<Item_Recommend> list_recommend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);

        txt_member_num = findViewById(R.id.txt_member_num);
        txt_selected_num_detail = findViewById(R.id.txt_selected_num);
        txt_slash = findViewById(R.id.txt_slash);


        listItems = new ArrayList<ListItem_Check_Join_Member>();
        list_career = new ArrayList<>();
        list_myskill = new ArrayList<>();
        list_interest = new ArrayList<>();
        list_recommend = new ArrayList<>();


        text_username = (TextView) findViewById(R.id.text_username);
        //  text_email = (TextView) itemView.findViewById(R.id.text_email);
        text_title = (TextView) findViewById(R.id.text_title);
        //text_content = (TextView) itemView.findViewById(R.id.text_content);
        text_content = (ReadMoreTextView) findViewById(R.id.text_content);
        text_history = (TextView) findViewById(R.id.text_history);
        txtOptionDigit = (TextView) findViewById(R.id.txtOptionDigit);
        txtOptionDigit_not_myproject = (TextView) findViewById(R.id.txtOptionDigit_not_myproject);
        btn_like = (ImageView) findViewById(R.id.btn_like);
        btn_unlike = (ImageView) findViewById(R.id.btn_unlike);
        btn_reply = (ImageView) findViewById(R.id.btn_reply);
        btn_join = (ImageView) findViewById(R.id.btn_join);
        btn_unjoin = (ImageView) findViewById(R.id.btn_unjoin);
        btn_confirm_project = findViewById(R.id.btn_confirm_project);
        btn_project_completed = findViewById(R.id.btn_project_completed);
        //   line_texts = (RelativeLayout) findViewById(R.id.line_texts);
        //txt_join = (TextView) findViewById(R.id.txt_join);
        // txt_reply = (TextView) findViewById(R.id.txt_reply);
        // txt_like = (TextView) findViewById(R.id.txt_like);
        line_join = (LinearLayout) findViewById(R.id.line_join);
        line_pic = (LinearLayout) findViewById(R.id.line_pic);
        prof_pic = (CircleImageView) findViewById(R.id.prof_pic);
        // join_member_pic = (CircleImageView) findViewById(R.id.join_member_pic);
        dev_duration = findViewById(R.id.dev_duration);
        apply_duration = findViewById(R.id.apply_duration);
        member_num = findViewById(R.id.member_num);
        gps = findViewById(R.id.gps);
        profile_expand = findViewById(R.id.profile_expand);
        profile_collapse = findViewById(R.id.profile_collapse);
        relative_profile = findViewById(R.id.relative_profile);
        recommend = findViewById(R.id.recommend);
        //scrollView = findViewById(R.id.scrollView);

        profile_expand.setOnClickListener(this);
        profile_collapse.setOnClickListener(this);

        project_img1 = findViewById(R.id.img1);
        project_img2 = findViewById(R.id.img2);
        project_img3 = findViewById(R.id.img3);
        project_img4 = findViewById(R.id.img4);
        project_img5 = findViewById(R.id.img5);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);

        toolbar = findViewById(R.id.toolBar_info);
        toolbar.setTitle("상세보기");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                //프래그먼트 중에서 홈화면이 제일 첫 화면으로 나오게 한다.
                finish();

                getSupportFragmentManager().beginTransaction().replace(R.id.activity_content_detail, new HomeFragment()).commit();

            }
        });

        //댓글관련
        //댓글 전송 버튼과 에디트 메세지.
        btn_send = findViewById(R.id.btn_send);
        btn_re_send = findViewById(R.id.btn_re_send);
        edit_reply = findViewById(R.id.edit_reply);
        text_reply_to = findViewById(R.id.text_reply_to);
        cancel = findViewById(R.id.cancel);

        // btn_send.setOnClickListener(this);
        // btn_re_send.setOnClickListener(this);


        // expLV = (ExpandableListView) findViewById(R.id.expLV);
        //  listCategorias = new ArrayList<Item>();
        // mapChild = new HashMap<>();


        //리사이클러뷰 아이템에서 가져온 프로젝트 아이디 값과, 이메일 값.
        Intent intent = getIntent();
        project_id = intent.getIntExtra("project_id", 0);
        project_email = intent.getStringExtra("project_email");


        project_email_detail = project_email;
        project_id_detail = project_id;


        if (project_email.equals(loadLoginEmail())) {
            line_join.setVisibility(View.GONE);
            btn_confirm_project.setVisibility(View.VISIBLE);
            txt_selected_num_detail.setVisibility(View.VISIBLE);
            txt_slash.setVisibility(View.VISIBLE);
        } else {
            line_join.setVisibility(View.VISIBLE);
            btn_confirm_project.setVisibility(View.GONE);
            btn_join.setVisibility(View.VISIBLE);

            txt_selected_num_detail.setVisibility(View.GONE);
            txt_slash.setVisibility(View.GONE);
        }

        //신청인원 리사이클러뷰 관련
        recyclerView_join_member = findViewById(R.id.recycler_view_join_member);
        recyclerView_join_member.setLayoutManager(new LinearLayoutManager(Content_Completed_Project_Detail_Activity.this));
        recyclerView_join_member.setHasFixedSize(true);


        recycler_view_skill = findViewById(R.id.recycler_view_skill);
        recycler_view_skill.setLayoutManager(new LinearLayoutManager(Content_Completed_Project_Detail_Activity.this));
        recycler_view_skill.setHasFixedSize(true);

        recycler_view_career = findViewById(R.id.recycler_view_career);
        recycler_view_career.setLayoutManager(new LinearLayoutManager(Content_Completed_Project_Detail_Activity.this));
        recycler_view_career.setHasFixedSize(true);

        recycler_view_interest = findViewById(R.id.recycler_view_interest);
        recycler_view_interest.setLayoutManager(new LinearLayoutManager(Content_Completed_Project_Detail_Activity.this));
        recycler_view_interest.setHasFixedSize(true);

        recycler_view_recommend = findViewById(R.id.recycler_view_recommend);
        recycler_view_recommend.setLayoutManager(new LinearLayoutManager(Content_Completed_Project_Detail_Activity.this));
        recycler_view_recommend.setHasFixedSize(true);

        uploadProject();
        uploadJoinMember();

        //관심분야 가져오기
        getInterest();
        //경력 가져오기
        getCareer();
        //보유기술 가져오기
        getMySkill();
        //추천서 가져오기
        getRecommend();
        //uploadReply();
        //리스트뷰 화살표 오른쪽으로 옮기기
        //changeArrow();
    }

    //프로젝트 업로드
    public void uploadProject() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("project_content");


                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);


                                String History = object.getString("history");

                                String Title = object.getString("title");

                                String Content = object.getString("content");
                                String Username = object.getString("username");
                                String eEmail = object.getString("email");
                                int Id = object.getInt("id");
                                int Like_count = object.getInt("like_count");
                                int Reply_count = object.getInt("reply_count");
                                int Join_count = object.getInt("join_count");
                                String Url = object.getString("profile");
                                int Selected_num = object.getInt("selected_num");
                                String Apply_duration = object.getString("apply_duration");
                                String Gps = object.getString("gps");
                                String Dev_duration = object.getString("dev_duration");
                                int Member_num = object.getInt("member_num");
                                int Img_count = object.getInt("img_count");
                                String Img_url1 = object.getString("img1");
                                String Img_url2 = object.getString("img2");
                                String Img_url3 = object.getString("img3");
                                String Img_url4 = object.getString("img4");
                                String Img_url5 = object.getString("img5");

                                String Text1 = object.getString("text1");
                                String Text2 = object.getString("text2");
                                String Text3 = object.getString("text3");
                                String Text4 = object.getString("text4");
                                String Text5 = object.getString("text5");

                                selected_count_detail = Selected_num;
                                txt_member_num.setText(Member_num + "");
                                txt_selected_num_detail.setText(Selected_num + "");
                                join_count_detail = Member_num;

                                gps.setText(Gps);
                                text_username.setText(Username);
                                // holder.text_email.setText(item.getEmail());
                                text_title.setText(Title);
                                text_content.setText(Content);
                                text_history.setText(History);
                                apply_duration.setText(Apply_duration);
                                dev_duration.setText(Dev_duration);
                                member_num.setText(Member_num + "");
                                //프로필 사진 출력
                                Picasso.with(Content_Completed_Project_Detail_Activity.this)
                                        .load(Url)
                                        .error(R.mipmap.ic_launcher_round)
                                        .fit()
                                        .into(prof_pic);


                                //서버에서 가지고온 프로젝트 관련 이미지 url값이 null이 아니라면 스크롤뷰를 visible상태로 만들고 해당 이미지뷰에 이미지를 입력.

                                if (!Img_url1.equals("null")) {
                                    line_pic.setVisibility(View.VISIBLE);
                                    project_img1.setVisibility(View.VISIBLE);
                                    project_img1.setScaleType(ImageView.ScaleType.FIT_XY);
                                    project_img1.setPadding(0, 0, 10, 0);
                                    Picasso.with(Content_Completed_Project_Detail_Activity.this).load(Img_url1).error(R.drawable.ic_account_circle_black_24dp).into(project_img1);
                                    text1.setText(Text1);
                                } else {

                                    project_img1.setVisibility(View.GONE);
                                }
                                if (!Img_url2.equals("null")) {
                                    line_pic.setVisibility(View.VISIBLE);
                                    project_img2.setVisibility(View.VISIBLE);
                                    project_img2.setScaleType(ImageView.ScaleType.FIT_XY);
                                    project_img2.setPadding(0, 0, 10, 0);
                                    Picasso.with(Content_Completed_Project_Detail_Activity.this).load(Img_url2).error(R.drawable.ic_account_circle_black_24dp).fit().into(project_img2);
                                    text2.setText(Text2);
                                } else {

                                    project_img2.setVisibility(View.GONE);

                                }
                                if (!Img_url3.equals("null")) {
                                    line_pic.setVisibility(View.VISIBLE);
                                    project_img3.setVisibility(View.VISIBLE);
                                    project_img3.setScaleType(ImageView.ScaleType.FIT_XY);
                                    project_img3.setPadding(0, 0, 10, 0);
                                    Picasso.with(Content_Completed_Project_Detail_Activity.this).load(Img_url3).error(R.drawable.ic_account_circle_black_24dp).fit().into(project_img3);
                                    text3.setText(Text3);
                                } else {

                                    project_img3.setVisibility(View.GONE);
                                }
                                if (!Img_url4.equals("null")) {
                                    line_pic.setVisibility(View.VISIBLE);
                                    project_img4.setVisibility(View.VISIBLE);
                                    project_img4.setScaleType(ImageView.ScaleType.FIT_XY);
                                    project_img4.setPadding(0, 0, 10, 0);
                                    Picasso.with(Content_Completed_Project_Detail_Activity.this).load(Img_url4).error(R.drawable.ic_account_circle_black_24dp).fit().into(project_img4);
                                    text4.setText(Text4);
                                } else {

                                    project_img4.setVisibility(View.GONE);
                                }

                                if (!Img_url5.equals("null")) {
                                    line_pic.setVisibility(View.VISIBLE);
                                    project_img5.setVisibility(View.VISIBLE);
                                    project_img5.setScaleType(ImageView.ScaleType.FIT_XY);
                                    project_img5.setPadding(0, 0, 10, 0);
                                    Picasso.with(Content_Completed_Project_Detail_Activity.this).load(Img_url5).error(R.drawable.ic_account_circle_black_24dp).fit().into(project_img5);
                                    text5.setText(Text5);
                                } else {

                                    project_img5.setVisibility(View.GONE);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Content_Completed_Project_Detail_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("project_id_from_detail", String.valueOf(project_id));

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Content_Completed_Project_Detail_Activity.this);
        requestQueue.add(stringRequest);

        // MySingleton.getInstance(getContext()).addToRequestque(stringRequest);
        // CustomVolleyRequest.getInstance(getContext()).addToRequestque(stringRequest);
        //CustomVolleyRequest.getInstance(getContext()).getRequestQueue();
    }

    //신청인원 불러오기
    public void uploadJoinMember() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_JOIN_MEMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("project_join");
                            // JSONObject data = jsonarray.getJSONObject(0);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                ListItem_Check_Join_Member item = new ListItem_Check_Join_Member();
                                System.out.println("신청한놈들은 : " + object.getString("join_username"));
                                item.username = object.getString("join_username");
                                item.url = object.getString("join_profile");
                                item.email = object.getString("join_email");
                                if (project_email.equals(loadLoginEmail())) {
                                    item.me = true;
                                } else {
                                    item.me = false;
                                }

                                if (object.getInt("selected") == 0) {
                                    item.together = false;
                                } else {
                                    item.together = true;
                                }
                                listItems.add(item);


                            }

                            RecyclerViewAdapter_Show_Join_Member_Detail adapter = new RecyclerViewAdapter_Show_Join_Member_Detail(Content_Completed_Project_Detail_Activity.this, listItems);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Content_Completed_Project_Detail_Activity.this, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView_join_member.setLayoutManager(layoutManager);
                            recyclerView_join_member.setAdapter(adapter);
                            //nestedscrollview가 매끄럽게 움직이게 해줌.
                            recyclerView_join_member.setNestedScrollingEnabled(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Content_Completed_Project_Detail_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();


                params.put("join_id", String.valueOf(project_id));

                return params;

            }
        };

        MySingleton.getInstance(Content_Completed_Project_Detail_Activity.this).addToRequestque(stringRequest);

    }


    //관심사 가져오기
    public void getInterest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INTEREST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("interest");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_Interesting item = new Item_Interesting();
                                item.reason = object.getString("reason");
                                item.section = object.getString("section");

                                System.out.println(item.reason);
                                System.out.println(item.section);
                                list_interest.add(item);


                            }
                            //데이터가 하나도 없으면 '더보기' 버튼을 안보이게 처리.
                           /* if (jsonarray.length() == 0) {
                                bottom_interest.setVisibility(View.GONE);

                            } else {
                                bottom_interest.setVisibility(View.VISIBLE);
                            }*/

                            RecyclerViewAdapter_Interesting adapter = new RecyclerViewAdapter_Interesting(Content_Completed_Project_Detail_Activity.this, list_interest);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Content_Completed_Project_Detail_Activity.this);

                            recycler_view_interest.setLayoutManager(layoutManager);
                            recycler_view_interest.setAdapter(adapter);
                            recycler_view_interest.setNestedScrollingEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Content_Completed_Project_Detail_Activity.this, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", project_email);
                return parameters;
            }
        };
        MySingleton.getInstance(Content_Completed_Project_Detail_Activity.this).addToRequestque(stringRequest);

    }

    //경력 가져오기
    public void getCareer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CAREER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("career");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_Career item = new Item_Career();
                                item.username = object.getString("username");
                                item.email = object.getString("email");
                                item.position = object.getString("position");
                                item.company = object.getString("company");
                                item.start_day = object.getString("start_day");
                                item.end_day = object.getString("end_day");
                                item.explain = object.getString("explain");

                                System.out.println(item.username);
                                System.out.println(item.email);
                                System.out.println(item.position);
                                System.out.println(item.company);
                                System.out.println(item.start_day);
                                System.out.println(item.end_day);
                                System.out.println(item.explain);

                                list_career.add(item);


                            }
                            //데이터가 하나도 없으면 '더보기' 버튼을 안보이게 처리.
                          /*  if (jsonarray.length() == 0) {
                                bottom_career.setVisibility(View.GONE);
                            } else {
                                bottom_career.setVisibility(View.VISIBLE);
                            }*/

                            RecyclerViewAdapter_Career adapter = new RecyclerViewAdapter_Career(Content_Completed_Project_Detail_Activity.this, list_career);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Content_Completed_Project_Detail_Activity.this);

                            recycler_view_career.setLayoutManager(layoutManager);

                            recycler_view_career.setAdapter(adapter);
                            recycler_view_career.setNestedScrollingEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Content_Completed_Project_Detail_Activity.this, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", project_email);
                return parameters;
            }
        };
        MySingleton.getInstance(Content_Completed_Project_Detail_Activity.this).addToRequestque(stringRequest);

    }


    //경력 가져오기
    public void getMySkill() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, MYSKILL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("MySkill");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_MySkill item = new Item_MySkill();
                                item.username = object.getString("username");
                                item.email = object.getString("email");
                                item.myskill = object.getString("myskill");
                                item.explain = object.getString("explain");


                                list_myskill.add(item);


                            }
                            //데이터가 하나도 없으면 '더보기' 버튼을 안보이게 처리.
                          /*  if (jsonarray.length() == 0) {
                                bottom_myskill.setVisibility(View.GONE);
                            } else {
                                bottom_myskill.setVisibility(View.VISIBLE);
                            }
*/
                            RecyclerViewAdapter_MySkill adapter = new RecyclerViewAdapter_MySkill(Content_Completed_Project_Detail_Activity.this, list_myskill);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Content_Completed_Project_Detail_Activity.this);

                            recycler_view_skill.setLayoutManager(layoutManager);

                            recycler_view_skill.setAdapter(adapter);
                            //nestedscrollview가 매끄럽게 움직이게 해줌.
                            recycler_view_skill.setNestedScrollingEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Content_Completed_Project_Detail_Activity.this, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", project_email);
                return parameters;
            }
        };
        MySingleton.getInstance(Content_Completed_Project_Detail_Activity.this).addToRequestque(stringRequest);

    }

    //추천서 가져오기
    public void getRecommend() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECOMMEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("recommend");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Item_Recommend item = new Item_Recommend();
                                item.username = object.getString("username");
                                item.email = object.getString("email");
                                item.url = object.getString("profile");
                                item.recommend_content = object.getString("recommend_content");
                                item.history = object.getString("history");


                                list_recommend.add(item);


                            }
                            //데이터가 하나도 없으면 '더보기' 버튼을 안보이게 처리.
                            /*if (jsonarray.length() == 0) {
                                bottom_recommend.setVisibility(View.GONE);
                            } else {
                                bottom_recommend.setVisibility(View.VISIBLE);
                            }*/

                            RecyclerViewAdapter_Recommend adapter = new RecyclerViewAdapter_Recommend(Content_Completed_Project_Detail_Activity.this, list_recommend);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Content_Completed_Project_Detail_Activity.this);

                            recycler_view_recommend.setLayoutManager(layoutManager);

                            recycler_view_recommend.setAdapter(adapter);
                            //nestedscrollview가 매끄럽게 움직이게 해줌.
                            recycler_view_recommend.setNestedScrollingEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Content_Completed_Project_Detail_Activity.this, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", project_email);
                return parameters;
            }
        };
        MySingleton.getInstance(Content_Completed_Project_Detail_Activity.this).addToRequestque(stringRequest);

    }


    //댓글 출력
  /*  public void uploadReply() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_REPLY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("reply");


                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                ArrayList<Item_child> list_child = new ArrayList<>();
                                String username = object.getString("username");

                                String message = object.getString("message");

                                String url = object.getString("profile");
                                int reply_count = object.getInt("reply_count");
                                int reply_id = object.getInt("id");


                                item = new Item(username, url, message, reply_count, reply_id);
                                item.open = false;

                                int index = listCategorias.indexOf(item);
                                int finalI = i;
                                //대댓글 목록 불러오기
                                if (item.re_reply_count != 0) {

                                    System.out.println("대댓글의 갯수가 0개가 아니라면 : " + item.re_reply_count);

                                    System.out.println("아이템의 i값은  " + finalI);
                                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, SHOW_Re_REPLY,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {
                                                        JSONObject jsonobject2 = new JSONObject(response);
                                                        JSONArray jsonarray2 = jsonobject2.getJSONArray("re_reply");
                                                        for (int k = 0; k < jsonarray2.length(); k++) {

                                                            JSONObject object2 = jsonarray2.getJSONObject(k);
                                                            String sub_username = object2.getString("re_username");
                                                            String sub_message = object2.getString("re_message");
                                                            String sub_url = object2.getString("re_profile");
                                                            //  list_child = new ArrayList<Item_child>();
                                                            Item_child item_child = new Item_child();
                                                            // item_child.re_reply_id = re_id;
                                                            //부모댓글 아이디 값과 자식 댓글이 가지고 있는 부모댓글의 아이디 값이 같으면
                                                            //item_child 안에 해당 값들을 넣어준다.
                                                            item_child.sub_message = sub_message;
                                                            item_child.sub_url = sub_url;
                                                            item_child.sub_username = sub_username;
                                                            list_child.add(item_child);


                                                        }
                                                        mapChild.put(listCategorias.get(finalI).reply_id, list_child);
                                                        //댓글 열어두기
                                                        expLV.expandGroup(finalI);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {

                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(Content_Detail_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }) {

                                        //해쉬맵을 통해 php에 이메일 값을 보내줌.
                                        @Override
                                        protected Map<String, String> getParams() {

                                            Map<String, String> params = new HashMap<String, String>();

                                            System.out.println("대댓글이 있고 해당 댓글의 아이디 값은 : " + reply_id);

                                            params.put("reply_id", String.valueOf(reply_id));

                                            return params;

                                        }
                                    };

                                    MySingleton.getInstance(Content_Detail_Activity.this).addToRequestque(stringRequest2);


                                } else {

                                    //mapChild.put(listCategorias.get(finalI).reply_id,null);
                                }


                                listCategorias.add(item);
                            }

                            Fragment_Reply main3 = new Fragment_Reply();
                            adapter = new RecyclerViewAdapter_Reply(listCategorias, mapChild, Content_Detail_Activity.this);
                            expLV.setAdapter(adapter);
                            //nestedscrollview가 매끄럽게 움직일 수 있도록 해주는 메소드.
                            expLV.setNestedScrollingEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Content_Detail_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                System.out.println(project_id);

                params.put("project_id", String.valueOf(project_id));

                return params;

            }
        };

        MySingleton.getInstance(Content_Detail_Activity.this).addToRequestque(stringRequest);


    }*/

    //리스트뷰 화살표 오른쪽으로 옮기기
 /*   public void changeArrow() {
        Display display = Content_Detail_Activity.this.getWindowManager().getDefaultDisplay();
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
    }*/

 //프로필 레이아웃 펼치기
 public void expandProfile(){
     relative_profile.setVisibility(View.VISIBLE);
     profile_expand.setVisibility(View.GONE);
     profile_collapse.setVisibility(View.VISIBLE);
 }

 //프로필 레이아웃 감추기
    public void collapseProfile(){
        relative_profile.setVisibility(View.GONE);
        profile_expand.setVisibility(View.VISIBLE );
        profile_collapse.setVisibility(View.GONE);
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.profile_expand:
                expandProfile();
                break;
            case R.id.profile_collapse:
                collapseProfile();
                break;
        }
    }
}
