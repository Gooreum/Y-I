package com.example.goo.test.Fragment.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Reply;
import com.example.goo.test.Item.Item;
import com.example.goo.test.Item.Item_child;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-18.
 */

public class Fragment_Reply extends AppCompatActivity implements View.OnClickListener {
    private static final String ADD_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/add_reply.php";
    private static final String SHOW_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_reply.php";
    private static final String SHOW_Re_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_re_reply.php";
    private static final String ADD_Re_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/add_re_reply.php";

    public Fragment_Reply() {
    }
    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;




    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private android.support.v7.widget.Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private int refresh_count = 0;
    public static int project_id_reply;
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

    Intent intent;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main3);

        //댓글 전송 버튼과 에디트 메세지.
        btn_send = findViewById(R.id.btn_send);
        btn_re_send = findViewById(R.id.btn_re_send);
        edit_reply = findViewById(R.id.edit_reply);
        text_reply_to = findViewById(R.id.text_reply_to);
        cancel = findViewById(R.id.cancel);

        //댓글을 눌럿을 때 보내준 게시판 아이디 값을 가지고 옴.
      /*  Bundle bundle = this.getArguments();
        if (bundle != null) {


        }
*/
        intent = getIntent();
        project_id_reply = intent.getIntExtra("project_id",0);
        System.out.println("게시글의 아이디 값은 : " +intent.getIntExtra("project_id",0));
        refreshLayout = findViewById(R.id.swipe_refresh);

        //툴바
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setTitle("댓글");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();

            }
        });

        btn_send.setOnClickListener(this);
        btn_re_send.setOnClickListener(this);


        expLV = (ExpandableListView) findViewById(R.id.expLV);

        //화살표 위치 바꾸기
        changeArrow();


        listCategorias = new ArrayList<Item>();

        mapChild = new HashMap<>();

        //위로 당기면 스와이프 레이아웃에 의해 새로고침이 됨.
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent=new Intent(Fragment_Reply.this,Fragment_Reply.class);
                intent.putExtra("project_id",project_id_reply);
                finish();
                startActivity(intent);

            }
        });
        refreshLayout.setRefreshing(false);


        uploadReply();



    }


    @Override
    protected void onResume() {
        super.onResume();
        //adapter.notifyDataSetChanged();
    }

    //댓글 출력
    public void uploadReply() {
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

                              //  int index = listCategorias.indexOf(item);
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
                                                    Toast.makeText(Fragment_Reply.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

                                    MySingleton.getInstance(Fragment_Reply.this).addToRequestque(stringRequest2);


                                } else {

                                    //mapChild.put(listCategorias.get(finalI).reply_id,null);
                                }


                                listCategorias.add(item);
                            }

                            Fragment_Reply main3 = new Fragment_Reply();
                            adapter = new RecyclerViewAdapter_Reply(listCategorias, mapChild, Fragment_Reply.this);
                            expLV.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Fragment_Reply.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                System.out.println(project_id_reply);

                params.put("project_id", String.valueOf(project_id_reply));

                return params;

            }
        };

        MySingleton.getInstance(Fragment_Reply.this).addToRequestque(stringRequest);


    }

    //전송버튼 메서드
    public void send() {
        System.out.println("부모 댓글에서 클릭한 겁니다ㅣ");
        //댓글 내용이 빈칸이면 예외처리 해주기.
        final String message = edit_reply.getText().toString();
        if (message.equals("") || message.equals(" ") || message.equals("  ") || message.equals("  ") || message.equals("  ")) {
            Toast.makeText(Fragment_Reply.this, "빈칸 없이 작성해주세요.", Toast.LENGTH_SHORT).show();
        } else {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_REPLY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                            if (response.contains("1")) {
                                Intent intent=new Intent(Fragment_Reply.this,Fragment_Reply.class);
                                intent.putExtra("project_id",project_id_reply);

                                finish();
                                startActivity(intent);

                                Toast.makeText(Fragment_Reply.this, "작성 되었습니다.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Fragment_Reply.this, "댓글 작성 실패.", Toast.LENGTH_LONG).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Fragment_Reply.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("project_id", String.valueOf(project_id_reply));
                    params.put("email", loadLoginEmail());
                    params.put("message", message);
                    params.put("history", getTime());
                    return params;

                }
            };

            MySingleton.getInstance(Fragment_Reply.this).addToRequestque(stringRequest);

        }

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

    public void refresh() {
        //전송 후 프래그먼트를 띄었다가 다시 붙임.
        listCategorias.removeAll(item);
        mapChild.remove(item_child);
        uploadReply();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                send();
                edit_reply.setText("");
                hideKeyboardFrom(Fragment_Reply.this, view);

                //전송 후 액티비티를 새로고침 시킴.

               // refresh();
                break;

        }
    }

    //댓글 전송후 올려져 있는 키보드를 내린다.
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }
    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}