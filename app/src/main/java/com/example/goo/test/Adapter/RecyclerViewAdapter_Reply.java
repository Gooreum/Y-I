package com.example.goo.test.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Fragment.Home.Fragment_Reply;
import com.example.goo.test.Fragment.MyInfo.MyInfoFragment_By_Friend;
import com.example.goo.test.Item.Item;
import com.example.goo.test.Item.Item_child;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.goo.test.Fragment.Home.Fragment_Reply.btn_re_send;
import static com.example.goo.test.Fragment.Home.Fragment_Reply.btn_send;
import static com.example.goo.test.Fragment.Home.Fragment_Reply.cancel;
import static com.example.goo.test.Fragment.Home.Fragment_Reply.edit_reply;
import static com.example.goo.test.Fragment.Home.Fragment_Reply.project_id_reply;
import static com.example.goo.test.Fragment.Home.Fragment_Reply.text_reply_to;

/**
 * Created by Goo on 2018-05-18.
 */

public class RecyclerViewAdapter_Reply extends BaseExpandableListAdapter {

    private static final String ADD_Re_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/add_re_reply.php";

    private ArrayList<Item> listCategoria;
    private Map<Integer, ArrayList<Item_child>> mapChild;

    private Context context;
    SwipeRefreshLayout swiper;

    Item items;
    Item_child items_child;

    TextView txt_message, reply_count;
    TextView txt_username;
    TextView txt_reply;

    TextView txt_message2, txt_reply2;
    TextView txt_username2;
    CircleImageView reply_img2;

    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public RecyclerViewAdapter_Reply(ArrayList<Item> listCategoria, Map<Integer, ArrayList<Item_child>> mapChild, Context context) {
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

        return this.mapChild.get(listCategoria.get(groupPosition).reply_id).size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return listCategoria.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mapChild.get(listCategoria.get(groupPosition).reply_id).get(childPosition);

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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.elv_group, null);

        }


        items = listCategoria.get(groupPosition);


        txt_username = convertView.findViewById(R.id.textView);
        txt_message = convertView.findViewById(R.id.txt_message);
        txt_reply = convertView.findViewById(R.id.txt_reply);
        reply_count = convertView.findViewById(R.id.reply_count);
        CircleImageView reply_img = convertView.findViewById(R.id.reply_img);
        Picasso.with(context).load(items.url).error(R.mipmap.ic_launcher_round)
                .into(reply_img);

        //친구 프로필 사진을 눌렀을 때 해당 친구의 프로필 화면으로 옮김.
        reply_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                System.out.println("sjldfkjsladkfjloskejdflkjweikfjwoeijf  " + items.getText());
                //친구의 이메일 값을 쉐어드로 저장해둠.
                SharedPreferences ID = context.getApplicationContext().getSharedPreferences("friend_email",  MODE_PRIVATE);
                SharedPreferences.Editor editor = ID.edit();
                editor.putString("friend_email", items.getText());
                editor.commit();

                MyInfoFragment_By_Friend myinfo = new MyInfoFragment_By_Friend();
                myinfo.setArguments(bundle);

                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, myinfo).addToBackStack(null)
                        .commit();

            }
        });



        txt_message.setText(items.message);
        txt_username.setText(items.getText());


        if (items.reply_id != 0) {

            reply_count.setText(items.re_reply_count + "개 더 보기");
        } else {

            reply_count.setText("");
        }


        int reply_id = items.reply_id;

        //댓글달기 눌렀을 때 리스트뷰가 아니라 프래그먼트에 붙어 있던 에디트 텍스트와 전송버튼을 위로 끌어 올려 댓글을 쓰게 하였음.
        //프래그먼트에 정의된 edittext, send_button을 static으로 주어 리스트뷰에서도 사용 가능하게 만들었음.
        //'댓글달기' 텍스트뷰를 클릭했을 때 키보드와 set.visibility(View.Gone)으로 되어 있던 ~누구님에게 댓글작성중이라는 텍스트뷰가 올라오도록 하였음.
        //
        txt_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.open = true;

                btn_send.setVisibility(View.GONE);
                btn_re_send.setVisibility(View.VISIBLE);
                //답글달기를 누르면 edittext에 포커스가 맞춰지고 키보드가 올라옴.
                edit_reply.requestFocus();

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                text_reply_to.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                //System.out.println("어떤놈에게 답글을 남길까~" +);

                text_reply_to.setText(listCategoria.get(groupPosition).getText() + "님에게 답글 남기는 중");
                edit_reply.setText("@" + listCategoria.get(groupPosition).getText());

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        btn_send.setVisibility(View.VISIBLE);
                        btn_re_send.setVisibility(View.GONE);
                        text_reply_to.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                        text_reply_to.setText("");
                        edit_reply.setText("");
                        hideKeyboardFrom(context, view);
                    }
                });

                //부모 댓글에 대한 답글
                btn_re_send.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        notifyDataSetChanged();
                        //댓글 내용이 빈칸이면 예외처리 해주기.
                        final String message = edit_reply.getText().toString();
                        if (message.equals("") || message.equals(" ") || message.equals("  ") || message.equals("  ") || message.equals("  ")) {
                            Toast.makeText(context, "빈칸 없이 작성해주세요.", Toast.LENGTH_SHORT).show();
                        } else {

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_Re_REPLY,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                                            if (response.contains("1")) {
                                                //hideKeyboardFrom(context, view);
                                                btn_send.setVisibility(View.VISIBLE);
                                                btn_re_send.setVisibility(View.GONE);
                                                text_reply_to.setVisibility(View.GONE);
                                                cancel.setVisibility(View.GONE);
                                                text_reply_to.setText("");
                                                edit_reply.setText("");
                                                hideKeyboardFrom(context, view);

                                                //대댓글 작성 후 화면 갱신
                                                Intent intent = new Intent(context,Fragment_Reply.class);
                                                intent.putExtra("project_id",project_id_reply);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                context.startActivity(intent);

                                                Toast.makeText(context, "작성 되었습니다.", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(context, "댓글 작성 실패.", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                        }


                                    }) {
                                @Override
                                protected Map<String, String> getParams() {

                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("reply_id", String.valueOf(reply_id));
                                    params.put("email", loadLoginEmail());
                                    params.put("message", message);
                                    params.put("history", getTime());
                                    params.put("project_id", String.valueOf(project_id_reply));
                                    return params;

                                }
                            };

                            MySingleton.getInstance(context).addToRequestque(stringRequest);
                        }
                    }
                });

            }
        });

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.elv_child, null);
        }

        items = listCategoria.get(groupPosition);
        items_child = (Item_child) getChild(groupPosition, childPosition);


        if(convertView.getId() == 0 ){
            items.setExpandable(false);

        }
        txt_message2 = convertView.findViewById(R.id.txt_message);
        txt_username2 = convertView.findViewById(R.id.textView);
        reply_img2 = convertView.findViewById(R.id.reply_img);
        txt_reply2 = convertView.findViewById(R.id.txt_reply2);
        System.out.println("reply_id   " + items.reply_id);
        System.out.println("re_reply_id   " + items_child.re_reply_id);
        txt_username2.setText(items_child.sub_username);
        txt_message2.setText(items_child.sub_message);

        Picasso.with(context).load(items_child.sub_url).error(R.mipmap.ic_launcher_round)
                .into(reply_img2);



        int reply_id = items.reply_id;
        System.out.println("'대'댓글달기 누른놈의 값은 : " + reply_id);

        //댓글달기 눌렀을 때 리스트뷰가 아니라 프래그먼트에 붙어 있던 에디트 텍스트와 전송버튼을 위로 끌어 올려 댓글을 쓰게 하였음.
        //프래그먼트에 정의된 edittext, send_button을 static으로 주어 리스트뷰에서도 사용 가능하게 만들었음.
        //'댓글달기' 텍스트뷰를 클릭했을 때 키보드와 set.visibility(View.Gone)으로 되어 있던 ~누구님에게 댓글작성중이라는 텍스트뷰가 올라오도록 하였음.
        //
        txt_reply2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.open = true;
                btn_send.setVisibility(View.GONE);
                btn_re_send.setVisibility(View.VISIBLE);
                //답글달기를 누르면 edittext에 포커스가 맞춰지고 키보드가 올라옴.
                edit_reply.requestFocus();

                //키보드 올려주기
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                text_reply_to.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                text_reply_to.setText( ((Item_child) getChild(groupPosition, childPosition)).sub_username + "님에게 답글 남기는 중");
                edit_reply.setText("@" + ((Item_child) getChild(groupPosition, childPosition)).sub_username);


                //대댓글 취소하기
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btn_send.setVisibility(View.VISIBLE);
                        btn_re_send.setVisibility(View.GONE);

                        text_reply_to.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                        text_reply_to.setText("");
                        edit_reply.setText("");
                        hideKeyboardFrom(context, view);
                        System.out.println("부모 댓글에서 클릭한 겁니다ㅣ");
                        //댓글 내용이 빈칸이면 예외처리 해주기.

                    }
                });


                //답글에 대한 답글
                btn_re_send.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        System.out.println("대댓글에서 클릭한 겁니다ㅣ");
                        //댓글 내용이 빈칸이면 예외처리 해주기.
                        final String message = edit_reply.getText().toString();
                        if (message.equals("") || message.equals(" ") || message.equals("  ") || message.equals("  ") || message.equals("  ")) {
                            Toast.makeText(context, "빈칸 없이 작성해주세요.", Toast.LENGTH_SHORT).show();
                        } else {

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_Re_REPLY,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                                            if (response.contains("1")) {
                                               // notifyDataSetChanged();
                                              //  hideKeyboardFrom(context, view);
                                                btn_send.setVisibility(View.VISIBLE);
                                                btn_re_send.setVisibility(View.GONE);
                                                text_reply_to.setVisibility(View.GONE);
                                                cancel.setVisibility(View.GONE);
                                                text_reply_to.setText("");
                                                edit_reply.setText("");
                                                hideKeyboardFrom(context, view);
                                                //대댓글 작성 후 화면 갱신

                                                Intent intent = new Intent(context,Fragment_Reply.class);
                                                intent.putExtra("project_id",project_id_reply);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                context.startActivity(intent);

                                                Toast.makeText(context, "작성 되었습니다.", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(context, "댓글 작성 실패.", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                        }


                                    }) {
                                @Override
                                protected Map<String, String> getParams() {

                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("reply_id", String.valueOf(reply_id));

                                    System.out.println("대댓글 : " + String.valueOf(reply_id));
                                    System.out.println("대댓글 : " + loadLoginEmail());
                                    System.out.println("대댓글 : " + message);
                                    params.put("email", loadLoginEmail());
                                    params.put("message", message);
                                    params.put("history", getTime());
                                    params.put("project_id", String.valueOf(project_id_reply));
                                    return params;

                                }
                            };

                            MySingleton.getInstance(context).addToRequestque(stringRequest);
                        }
                    }
                });

            }
        });


        return convertView;

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    //댓글 전송후 올려져 있는 키보드를 내린다.
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = context.getSharedPreferences("Login", MODE_PRIVATE);
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

    public void setMyList(ArrayList list ){
        this.listCategoria = list;
        this.notifyDataSetChanged();
    }
}
